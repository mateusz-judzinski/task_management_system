package com.taskmanagement.taskmanagement.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jdk.jfr.Enabled;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {
    private int id;
    private String username;
    private String email;
    private String role;
    private List<Task> tasks;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task tempTask){

        if(tasks == null){
            tasks = new ArrayList<>();
        }
        c

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
