package com.example.assignmate.Models;

public class Todo_model {
    private String text;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Todo_model(String text, String email, boolean isChecked) {
        this.text = text;
        this.email = email;
        this.isChecked = isChecked;
    }

    private boolean isChecked;

    Todo_model(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Todo_model(String text, boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
    }
}
