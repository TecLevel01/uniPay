package com.matt.unipay.activities;

import static com.matt.unipay.util.Strings.scourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matt.unipay.R;
import com.matt.unipay.model.CourseItem;
import com.matt.unipay.util.Spacing;
import com.matt.unipay.util.Strings;
import com.matt.unipay.util.Util;

public class Courses extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recView;
    private FirestoreRecyclerAdapter<CourseItem, CourseHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        mInit();
        getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Add Course");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == 1) {
            Util.showCourseSheet(this, null);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        FirestoreRecyclerOptions<CourseItem> options = new FirestoreRecyclerOptions.Builder<CourseItem>()
                .setLifecycleOwner(this)
                .setQuery(Util.courseRef, snapshot -> {
                    CourseItem courseItem = snapshot.toObject(CourseItem.class);
                    courseItem.setCid(snapshot.getId());
                    return courseItem;
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<CourseItem, CourseHolder>(options) {
            @NonNull
            @Override
            public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.payment_item, parent, false);
                return new CourseHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull CourseItem model) {
                holder.setCourseData(model);
            }

            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() == 0) {
                    Util.snackbar(Courses.this, "No data available");
                }
                Util.hideProgress(Courses.this.getWindow().getDecorView().getRootView());
            }
        };
        recView.setAdapter(adapter);
    }

    private void mInit() {
        db = FirebaseFirestore.getInstance();
        recView = findViewById(R.id.recView);
        recView.addItemDecoration(new Spacing.LinearItemDecoration(20, 20, 0));
    }

    static class CourseHolder extends RecyclerView.ViewHolder {

        private final TextView tv1, tv2, tv3;
        private final ImageView imgViewEnd, imgView;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            imgViewEnd = itemView.findViewById(R.id.imageViewEnd);
            imgView = itemView.findViewById(R.id.imgView);
            imgView.setImageResource(R.drawable.ic_outline_library_books_24);
            imgViewEnd.setImageResource(R.drawable.ic_baseline_edit_24);

            tv3.setVisibility(View.GONE);

        }

        private void setCourseData(CourseItem courseItem) {
            tv1.setText(courseItem.getName());
            tv2.setText(courseItem.getTuition());

            imgViewEnd.setOnClickListener(view -> {
                Util.showCourseSheet(itemView.getContext(), courseItem);
            });
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), StudentView.class);
                intent.putExtra(scourse, courseItem.getName());
                intent.putExtra(Strings.stuition, courseItem.tuition);
                view.getContext().startActivity(intent);
            });
        }
    }
}