package com.taskmanagement.taskmanagement.entity;


<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> entity-branch
import jdk.jfr.Enabled;

import java.util.ArrayList;
import java.util.List;

@Entity
<<<<<<< HEAD
@Table
public class User {
    private int id;
=======
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
>>>>>>> entity-branch
    private String username;
    @Column(name = "email")
    private String email;
<<<<<<< HEAD
    private String role;
=======
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.DETACH, CascadeType.REFRESH})
>>>>>>> entity-branch
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

<<<<<<< HEAD
    public void add(Task tempTask){

        if(tasks == null){
            tasks = new ArrayList<>();
        }
        

    }
=======
>>>>>>> entity-branch

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
<<<<<<< HEAD
=======


    public void add(Task task){

        if(tasks == null){
            tasks = new ArrayList<>();
        }

        tasks.add(task);

        task.setUser(this);

    }
>>>>>>> entity-branch
}
