package com.inhatc.finddogapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private EditText editCaptchaAnswer;
    private TextView textCaptchaQuestion;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private int correctCaptchaAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editCaptchaAnswer = findViewById(R.id.editCaptchaAnswer);
        textCaptchaQuestion = findViewById(R.id.textCaptchaQuestion);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        generateCaptchaQuestion(); // 최초 1회 캡챠 생성

        btnSignUp.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String captchaInput = editCaptchaAnswer.getText().toString().trim();

            // 기본 유효성 검사
            if (email.isEmpty() || password.isEmpty() || captchaInput.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "비밀번호는 최소 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 캡챠 정답 확인
            try {
                int userAnswer = Integer.parseInt(captchaInput);
                if (userAnswer != correctCaptchaAnswer) {
                    Toast.makeText(this, "캡챠 정답이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    generateCaptchaQuestion();
                    editCaptchaAnswer.setText("");
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "캡챠에 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 회원가입 시도
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "회원가입 성공! 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, "이미 가입된 이메일입니다. 다른 이메일을 사용해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "회원가입 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            generateCaptchaQuestion();
                            editCaptchaAnswer.setText("");
                        }
                    });
        });
    }

    private void generateCaptchaQuestion() {
        Random rand = new Random();
        int a = rand.nextInt(10);
        int b = rand.nextInt(10);
        correctCaptchaAnswer = a + b;
        textCaptchaQuestion.setText("다음 계산의 결과는? " + a + " + " + b + " = ?");
    }
}