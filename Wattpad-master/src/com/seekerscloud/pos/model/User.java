package com.seekerscloud.pos.model;

public class User {
    private String email;
    private String fullName;
    private String contact;
    private String password;

    public User() {
    }

    public User(String email, String fullName, String contact, String password) {
        this.email = email;
        this.fullName = fullName;
        this.contact = contact;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
