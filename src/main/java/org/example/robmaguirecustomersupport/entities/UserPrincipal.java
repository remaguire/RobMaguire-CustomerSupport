package org.example.robmaguirecustomersupport.entities;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;

import java.io.Serial;
import java.io.Serializable;
import java.security.Principal;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UserPrincipal_Username", columnNames = "Username")
})
public class UserPrincipal implements Principal, Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String SESSION_ATTRIBUTE_KEY = "org.example.robmaguirecustomersupport.userprincipal.principal";
    private long userId;
    private String username;
    private byte[] hashedPassword;
    private boolean admin;

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    @Transient
    public String getName() {
        return username;
    }

    @Basic
    @Column(name = "Username")
    public String getUsername() {
        return username;
    }

    @Basic
    @Column(name = "Admin")
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "HashedPassword")
    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public static Principal getPrincipal(HttpSession session) {
        return (session == null ? null : (Principal) session.getAttribute(SESSION_ATTRIBUTE_KEY));
    }

    public static void setPrincipal(HttpSession session, Principal principal) {
        session.setAttribute(SESSION_ATTRIBUTE_KEY, principal);
    }
}
