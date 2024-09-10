package io.github.guisso.taskmanagement.entity;

/**
 * Class User
 * Represents a user entity with personal and authentication details
 * 
 * @version 1.0, 2024-09-08
 */

import java.time.LocalDate;

public class User extends Entity {

    private String name;
    private String email;
    private String password;
    private LocalDate lastAccess;
    private boolean active = true;

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() <= 150) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must be non-null and up to 150 characters.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.length() <= 255) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email must be non-null and up to 255 characters.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 4 && password.length() <= 8) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be non-null and between 4 to 8 characters.");
        }
    }

    public LocalDate getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDate lastAccess) {
        this.lastAccess = lastAccess;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    //</editor-fold>
}