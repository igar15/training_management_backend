package com.igar15.training_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.igar15.training_management.entity.abstracts.AbstractNamedEntity;
import com.igar15.training_management.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(example = "john@gmail.com")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @JsonIgnore
    @Column(name = "registered")
    @NotNull
    private Date registered = new Date();

    @JsonIgnore
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

//    @JsonIgnore
    @Column(name = "enabled")
    private boolean enabled = false;

//    @JsonIgnore
    @Column(name = "is_non_locked")
    private boolean isNonLocked = true;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Schema(example = "ROLE_USER")
    private Role role;



    public User() {
    }

    public User(Long id, String name, String email, String password, Date registered, String emailVerificationToken, boolean enabled, boolean isNonLocked, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.emailVerificationToken = emailVerificationToken;
        this.enabled = enabled;
        this.isNonLocked = isNonLocked;
        this.role = role;
    }

    public User(Long id, String name, String email, String password, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String name, String email, String password, boolean enabled, boolean isNonLocked, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean emailVerificationStatus) {
        this.enabled = emailVerificationStatus;
    }

    public boolean getIsNonLocked() {
        return isNonLocked;
    }

    public void setIsNonLocked(boolean nonLocked) {
        isNonLocked = nonLocked;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    @Schema(example = "John Doe")
    public String getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", isNonLocked=" + isNonLocked +
                ", role=" + role +
                '}';
    }
}
