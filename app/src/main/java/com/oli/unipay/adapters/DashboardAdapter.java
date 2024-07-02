package com.oli.unipay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oli.unipay.R;
import com.oli.unipay.model.DashboardItem;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    Context context;
    ArrayList<DashboardItem> dashboardItems;
    int iconId;

    public DashboardAdapter(Context context, ArrayList<DashboardItem> dashboardItems, int iconId) {
        this.context = context;
        this.dashboardItems = dashboardItems;
        this.iconId = iconId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(iconId);
        holder.tv1.setText(dashboardItems.get(position).getTitle());
        holder.tv2.setText(dashboardItems.get(position).getContent());

        if (dashboardItems.get(position).getTitle().contains("Paid")) {
            holder.tv2.setTextColor(context.getResources().getColor(R.color.primary_500));
        }
    }

    @Override
    public int getItemCount() {
        return dashboardItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
}
