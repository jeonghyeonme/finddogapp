package com.inhatc.finddogapp;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View view;
    private final Activity context;

    public CustomInfoWindowAdapter(Activity context) {
        this.context = context;
        this.view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null; // 기본 테두리 사용
    }

    @Override
    public View getInfoContents(Marker marker) {
        ImageView image = view.findViewById(R.id.image);
        TextView description = view.findViewById(R.id.description);

        String desc = marker.getSnippet();
        description.setText(desc != null ? desc : "설명 없음");

        // 마커에 Tag로 이미지 URL 저장되어 있음
        String imageUrl = (String) marker.getTag();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(image);
        } else {
            image.setImageResource(android.R.drawable.ic_menu_report_image); // 기본 이미지
        }

        return view;
    }
}