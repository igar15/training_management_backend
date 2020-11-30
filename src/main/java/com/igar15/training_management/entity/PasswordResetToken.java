package com.igar15.training_management.entity;

import com.igar15.training_management.entity.abstracts.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
