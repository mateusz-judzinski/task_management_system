package com.taskmanagement.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password should be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, " +
                    "one uppercase letter, and be at least 8 characters long")
    @Column(name = "password")
    private String password;
    @Email(message = "Email should be valid")
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


    public void add(Task task){

        if(tasks == null){
            tasks = new ArrayList<>();
        }

        tasks.add(task);

        task.setUser(this);
    }
}
