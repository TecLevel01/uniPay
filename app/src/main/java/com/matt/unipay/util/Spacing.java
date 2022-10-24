package com.matt.unipay.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Spacing {

    public static class LinearItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;
        private final int spaceHorizontal;
        private final int orientation;

        public LinearItemDecoration(int space, int spaceHorizontal, int orientation) {
            this.space = space;
            this.spaceHorizontal = spaceHorizontal;
            this.orientation = orientation;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            outRect.right = spaceHorizontal;

            // Vertical
            if (orientation == 0) {
                outRect.left = spaceHorizontal;
                outRect.bottom = space;
            }

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                if (orientation == 0) {
                    // Vertical
                    outRect.top = space;
                } else {
                    // Horizontal
                    outRect.left = spaceHorizontal;
                }
            }
        }
    }

    public static class GridSpacing extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacing(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
