package com.example.musicmentor.musicmentor;

public class Users {
    String name, status;

    public Users() {

    }

    public Users(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String name) {
        this.status = name;
    }
}
