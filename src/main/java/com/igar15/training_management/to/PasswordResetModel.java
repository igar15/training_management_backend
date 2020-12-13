package com.igar15.training_management.to;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema
public class PasswordResetModel {

    @NotBlank(message = "Password reset token must not be blank")
    @Schema(example = "sd34234dfAfsdf34234234sdfasfdsFAF343dsf324234dsfsdf345345sdffdgdfg")
    private String token;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, max = 32, message = "Password length must be between 5 and 32 characters")
    @Schema(example = "12345QweRty")
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
