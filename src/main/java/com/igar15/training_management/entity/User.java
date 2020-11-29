package com.igar15.training_management.entity;

import com.igar15.training_management.entity.abstracts.AbstractNamedEntity;
import com.igar15.training_management.entity.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {

    @Column(name = "email")
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Column(name = "password")
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @Column(name = "registered")
    @NotNull
    private Date registered = new Date();

    @Column(name = "email_verification_token")
    @NotNull
    private String emailVerificationToken;

    @Column(name = "email_verification_status")
    private boolean emailVerificationStatus = false;

    @Column(name = "is_non_locked")
    private boolean isNonLocked = true;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;



    public User() {
    }

    public User(long id, String name, String email, String password, Date registered, String emailVerificationToken, boolean emailVerificationStatus, boolean isNonLocked, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationStatus = emailVerificationStatus;
        this.isNonLocked = isNonLocked;
        this.role = role;
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

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public boolean isEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public boolean isNonLocked() {
        return isNonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        isNonLocked = nonLocked;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", emailVerificationStatus=" + emailVerificationStatus +
                ", isNonLocked=" + isNonLocked +
                ", role=" + role +
                '}';
    }
}
