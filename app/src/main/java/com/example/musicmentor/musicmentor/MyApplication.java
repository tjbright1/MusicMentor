package com.example.musicmentor.musicmentor;

import android.app.Application;

public class MyApplication extends Application {
    private String groupId;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String someVariable) {
        this.groupId = someVariable;
    }
}
