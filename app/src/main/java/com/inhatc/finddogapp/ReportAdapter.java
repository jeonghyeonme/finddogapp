package com.inhatc.finddogapp;

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

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
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
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_menu_gallery)
                    .into(holder.imageViewThumbnail);
        } else {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewDescription;
        TextView textViewLocation;  // ✅ 위치 정보 텍스트뷰 추가

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);  // ✅ 바인딩 추가
        }
    }
}