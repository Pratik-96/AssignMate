package com.example.assignmate.Models;

public class Streak_model {
    private String uname;
    private String uemail;

    private int currentStreak;

    private int longestStreak;

    private int longestStreakNeg;

    Streak_model(){}
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public int getLongestStreakNeg() {
        return longestStreakNeg;
    }

    public void setLongestStreakNeg(int longestStreakNeg) {
        this.longestStreakNeg = longestStreakNeg;
    }

    public Streak_model(String uname, String uemail, int currentStreak, int longestStreak, String lastLoginDate, int longestStreakNeg) {
        this.uname = uname;
        this.uemail = uemail;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastLoginDate = lastLoginDate;
        this.longestStreakNeg = longestStreakNeg;
    }

    private String lastLoginDate;

}
