package com.example.musicmentor.musicmentor;

import android.app.Application;

public class MyApplication extends Application {
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String someVariable) {
        this.groupId = someVariable;
    }
}
