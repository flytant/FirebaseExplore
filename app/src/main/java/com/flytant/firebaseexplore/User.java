package com.flytant.firebaseexplore;

public class User {

    private String userId;

    private String email;

    private String password;

    private String image;

    private String name;

    public User() {
    }

    public User(String userId, String email, String password, String image, String name) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.image = image;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
