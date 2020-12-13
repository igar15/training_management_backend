package com.igar15.training_management.to.swaggerTo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class SwaggerUserLoginTo {

    @Schema(example = "john@gmail.com")
    private String email;

    @Schema(example = "12345QweRty")
    private String password;

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
}
