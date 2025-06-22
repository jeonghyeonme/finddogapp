package com.inhatc.finddogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportPreviewBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_REPORT_ID = "reportId";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_URL = "imageUrl";
    private static final String ARG_USER_ID = "userId";

    public static ReportPreviewBottomSheet newInstance(Report report) {
        ReportPreviewBottomSheet fragment = new ReportPreviewBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_REPORT_ID, report.getId());
        args.putString(ARG_DESCRIPTION, report.getDescription());
        args.putString(ARG_IMAGE_URL, report.getImageUrl());
        args.putString(ARG_USER_ID, report.getUserId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_report_preview, container, false);

        ImageView imageView = view.findViewById(R.id.imagePreview);
        TextView textDescription = view.findViewById(R.id.textPreviewDescription);
        TextView textReporter = view.findViewById(R.id.textPreviewReporter);
        Button buttonDetail = view.findViewById(R.id.buttonDetailView);

        String reportId = getArguments().getString(ARG_REPORT_ID);
        String description = getArguments().getString(ARG_DESCRIPTION);
        String imageUrl = getArguments().getString(ARG_IMAGE_URL);
        String userId = getArguments().getString(ARG_USER_ID);

        textDescription.setText(description);
        String nickname = NicknameManager.getNicknameForUser(requireContext(), userId);
        textReporter.setText("작성자: " + nickname);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        }

        buttonDetail.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReportDetailActivity.class);
            intent.putExtra("reportId", reportId);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("description", description);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        return view;
    }
}