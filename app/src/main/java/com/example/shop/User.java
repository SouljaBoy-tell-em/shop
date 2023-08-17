package com.example.shop;

public class User {

    String userId, name, profile;

    public User(String userId, String name, String profile) {

        this.userId = userId;
        this.name = name;
        this.profile = profile;
    }

    public User() {}

    public String getUserId() {

        return userId;
    }

    public String getName() {

        return name;
    }

    public String getProfile() {

        return profile;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setProfile(String profile) {

        this.profile = profile;
    }
}