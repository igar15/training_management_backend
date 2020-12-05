package com.igar15.training_management.to;


public class PasswordResetModel {

    private String token;
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordResetModel{" +
                "token='" + token + '\'' +
                '}';
    }
}
