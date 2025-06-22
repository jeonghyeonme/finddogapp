package com.inhatc.finddogapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textDescription;
    private TextView textReporter;
    private EditText editComment;
    private Button buttonSubmit;
    private RecyclerView recyclerComments;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    private String reportId;
    private String reporterUserId;
    private DatabaseReference commentsRef;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_detail);

        // 뷰 바인딩
        imageView = findViewById(R.id.imageViewDetailImage);
        textDescription = findViewById(R.id.textViewDetailDescription);
        textReporter = findViewById(R.id.textViewReporter);
        editComment = findViewById(R.id.editTextComment);
        buttonSubmit = findViewById(R.id.buttonSubmitComment);
        recyclerComments = findViewById(R.id.recyclerViewComments);

        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList);
        recyclerComments.setAdapter(commentAdapter);

        // 인텐트 데이터 수신
        reportId = getIntent().getStringExtra("reportId");
        reporterUserId = getIntent().getStringExtra("userId");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");

        if (reportId == null) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 이미지 표시
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        }

        textDescription.setText(description);

        // 현재 로그인된 사용자 ID → 댓글용 닉네임 생성 시 사용
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 신고 작성자 닉네임 표시
        String displayNickname = NicknameManager.getNicknameForUser(this, reporterUserId);
        textReporter.setText("작성자: " + displayNickname);

        // Firebase DB 참조
        commentsRef = FirebaseDatabase.getInstance()
                .getReference("reports")
                .child(reportId)
                .child("comments");

        loadComments();

        // 댓글 작성
        buttonSubmit.setOnClickListener(v -> {
            String commentText = editComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                long timestamp = System.currentTimeMillis();
                String nickname = NicknameManager.getNicknameForUser(this, currentUserId);
                Comment comment = new Comment(nickname, commentText, timestamp);
                commentsRef.push().setValue(comment);
                editComment.setText("");
                Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        commentsRef.orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Comment c = data.getValue(Comment.class);
                            commentList.add(c);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReportDetailActivity.this, "댓글 불러오기 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}