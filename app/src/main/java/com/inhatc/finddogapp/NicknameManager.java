package com.inhatc.finddogapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

public class NicknameManager {

    private static final String PREFS_NAME = "AnonymousPrefs";
    private static final String KEY_MY_NICKNAME = "my_nickname";

    // ✅ [1] 내 닉네임 (앱 설치 시 고정)
    public static String getMyNickname(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String nickname = prefs.getString(KEY_MY_NICKNAME, null);
        if (nickname == null) {
            nickname = generateRandomNickname();
            prefs.edit().putString(KEY_MY_NICKNAME, nickname).apply();
        }
        return nickname;
    }

    // ✅ [2] 다른 사용자(userId)의 닉네임 고정 생성
    public static String getNicknameForUser(Context context, String userId) {
        if (userId == null || userId.isEmpty()) {
            return "익명";
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String key = "user_" + userId;
        String nickname = prefs.getString(key, null);
        if (nickname == null) {
            nickname = generateRandomNickname();
            prefs.edit().putString(key, nickname).apply();
        }
        return nickname;
    }

    // ✅ [공통] 익명 닉네임 생성 로직
    private static String generateRandomNickname() {
        int num = new Random().nextInt(9000) + 1000;
        return "익명" + num;
    }
}