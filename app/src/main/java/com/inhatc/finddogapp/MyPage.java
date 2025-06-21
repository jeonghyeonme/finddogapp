package com.inhatc.finddogapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);


        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        imageView = findViewById(R.id.imageView);
        Button btnPickImage = findViewById(R.id.btnPickImage);
        Button btnReportList = findViewById(R.id.btnReportList);
        Button btnWithdraw = findViewById(R.id.btnWithdraw);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnLogOut = findViewById(R.id.btnLogOut);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvUserEmail.setText(user.getEmail());
        }


        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        btnReportList.setOnClickListener(v -> {
            Intent intent = new Intent(MyPage.this, ReportListActivity.class);
            startActivity(intent);
        });

        // 회원탈퇴 기능
        btnWithdraw.setOnClickListener(v -> {
            new AlertDialog.Builder(MyPage.this)
                    .setTitle("회원 탈퇴")
                    .setMessage("정말 탈퇴하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MyPage.this, "회원 탈퇴 완료", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyPage.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(MyPage.this, "탈퇴 실패: 다시 로그인 후 시도하세요", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("아니요", null)
                    .show();
        });


        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MyPage.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // 로그아웃 기능
        btnLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MyPage.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyPage.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }
}
