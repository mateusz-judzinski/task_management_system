package com.taskmanagement.taskmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

<<<<<<< HEAD
    private int id;
    private String title;
    private String description;
    private int priority;
    private int userId;
=======
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "priority")
    private int priority;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "user_id")
    private User user;
>>>>>>> entity-branch

    public Task() {
    }

    public Task(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

<<<<<<< HEAD
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
=======
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
         this.user = user;
>>>>>>> entity-branch
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
<<<<<<< HEAD
                ", userId=" + userId +
=======
                ", user=" + user +
>>>>>>> entity-branch
                '}';
    }
}
