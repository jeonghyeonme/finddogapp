package com.inhatc.finddogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REPORT = 3001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Button btnMap = findViewById(R.id.btnMap);
        Button btnReport = findViewById(R.id.btnReport);
        Button btnMyPage = findViewById(R.id.btnMyPage);

        // 신고 버튼 클릭 → ReportActivity로 이동
        btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REPORT);
        });

        // 마이페이지: 아직 미구현
        btnMyPage.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "마이페이지 기능은 준비 중입니다.", Toast.LENGTH_SHORT).show();
        });

        // 지도 보기: 기존 로직 유지 (추후 필요시 수정)
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainMapActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 신고 후 돌아올 때 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REPORT && resultCode == RESULT_OK) {
            Toast.makeText(this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}