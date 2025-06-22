package com.inhatc.finddogapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;
    private Context context;

    public ReportAdapter(Context context, List<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.textViewDescription.setText(report.getDescription());

        String locationText = "위치: " + report.getLatitude() + ", " + report.getLongitude();
        holder.textViewLocation.setText(locationText);

        String imageUrl = report.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_menu_gallery)
                    .into(holder.imageViewThumbnail);
        } else {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_menu_gallery);
        }

        // ✅ 클릭 시 상세 화면으로 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("reportId", report.getId());
            intent.putExtra("userId", report.getUserId());
            intent.putExtra("imageUrl", report.getImageUrl());
            intent.putExtra("description", report.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewDescription;
        TextView textViewLocation;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
        }
    }
}