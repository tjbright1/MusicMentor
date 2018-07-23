package com.example.musicmentor.musicmentor;

public class Users {
    String name;
    String credentials;
    String age;
    String price;
    String userType;
    String level;

    public String getInstrument() {
        return instrument;
    }

    String instrument;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Users(String name, String credentials, String age, String price, String userType, String level, String instrument) {
        this.name = name;
        this.credentials = credentials;
        this.age = age;
        this.price = price;
        this.userType = userType;
        this.level = level;
        this.instrument = instrument;
    }

    public Users() {
    }

}
