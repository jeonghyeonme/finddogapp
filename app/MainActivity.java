package com.inhatc.finddogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnMap, btnReport, btnMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMap = findViewById(R.id.btnMap);
        btnReport = findViewById(R.id.btnReport);
        btnMyPage = findViewById(R.id.btnMyPage);

        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainMapActivity.class);
            startActivity(intent);
        });

        btnReport.setOnClickListener(v -> {
            // 추후 신고 액티비티로 연결
        });

        btnMyPage.setOnClickListener(v -> {
            // 추후 마이페이지 액티비티로 연결
        });
    }
}
