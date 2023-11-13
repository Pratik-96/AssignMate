package com.example.assignmate.Models;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class User {
    private String uid;

    private String name;

    private String sem;

    User(){}


    public User(String uid, String name, String sem) {
        this.uid = uid;
        this.name = name;
        this.sem = sem;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}
