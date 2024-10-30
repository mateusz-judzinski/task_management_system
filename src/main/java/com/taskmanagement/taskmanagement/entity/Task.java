package com.taskmanagement.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotNull(message = "Task title is required")
    @NotBlank(message = "Task title cannot be blank")
    @Size(min = 3, max = 50, message = "Task title should be between 3 and 50 characters")
    @Column(name = "title")
    private String title;
    @NotNull(message = "Task description is required")
    @NotBlank(message = "Task description cannot be blank")
    @Size(max = 200, message = "Description should be less than 200 characters")
    @Column(name = "description")
    private String description;
    @NotNull(message = "Priority is required")
    @Min(1)
    @Max(5)
    @Column(name = "priority")
    private int priority;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
         this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
