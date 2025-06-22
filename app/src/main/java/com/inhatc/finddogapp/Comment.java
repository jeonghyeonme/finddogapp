package com.inhatc.finddogapp;

public class Comment {
    private String nickname;   // 예: "익명"
    private String text;       // 댓글 내용
    private long timestamp;    // 작성 시간 (정렬용)

    public Comment() {
        // Firebase에서 객체 매핑을 위해 필요
    }

    public Comment(String nickname, String text, long timestamp) {
        this.nickname = nickname;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getter & Setter
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}