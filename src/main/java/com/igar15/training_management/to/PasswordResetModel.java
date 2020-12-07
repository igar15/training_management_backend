package com.igar15.training_management.to;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordResetModel {

    @NotBlank(message = "Password reset token must not be blank")
    private String token;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, max = 32, message = "Password length must be between 5 and 32 characters")
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
