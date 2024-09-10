/*
 * CC BY-NC-SA 4.0
 *
 * Copyright 2022 Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;.
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 *   Share - copy and redistribute the material in any medium or format
 *   Adapt - remix, transform, and build upon the material
 *
 * Under the following terms:
 *   Attribution - You must give appropriate credit, provide 
 *   a link to the license, and indicate if changes were made.
 *   You may do so in any reasonable manner, but not in any 
 *   way that suggests the licensor endorses you or your use.
 *   NonCommercial - You may not use the material for commercial purposes.
 *   ShareAlike - If you remix, transform, or build upon the 
 *   material, you must distribute your contributions under 
 *   the same license as the original.
 *   No additional restrictions - You may not apply legal 
 *   terms or technological measures that legally restrict 
 *   others from doing anything the license permits.
 *
 * Notices:
 *   You do not have to comply with the license for elements 
 *   of the material in the public domain or where your use 
 *   is permitted by an applicable exception or limitation.
 *   No warranties are given. The license may not give you 
 *   all of the permissions necessary for your intended use. 
 *   For example, other rights such as publicity, privacy, 
 *   or moral rights may limit how you use the material.
 */
package io.github.guisso.taskmanagement.task;

import io.github.guisso.taskmanagement.entity.Entity;
import java.time.LocalDate;

/**
 * Task class
 *
 * @author Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;
 * @version 0.2, 2024-08-29
 */
public class Task extends Entity {

    private String name;
    private String email;
    private String password;
    private LocalDate lastAccess;
    private Boolean active;

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Task() {
    }

    public Task(Long id, String name, String email, String password, LocalDate lastAccess, Boolean active) {
        setId(id);
        setName(name);
        setEmail(email);
        setPassword(password);
        setLastAccess(lastAccess);
        setActive(active);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public String getName() {
        return name;
    }

    public final void setName(String name) {
        if (name == null || name.length() > 150) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        if (email == null || email.length() > 255) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public final void setPassword(String password) {
        if (password == null || password.length() != 64) {
            throw new IllegalArgumentException("Password must be exactly 64 characters long");
        }
        this.password = password;
    }

    public LocalDate getLastAccess() {
        return lastAccess;
    }

    public final void setLastAccess(LocalDate lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Boolean getActive() {
        return active;
    }

    public final void setActive(Boolean active) {
        this.active = active;
    }
    // </editor-fold>

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lastAccess=" + lastAccess +
                ", active=" + active +
                '}';
    }
}