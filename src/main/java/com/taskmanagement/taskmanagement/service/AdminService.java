package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;

import java.util.List;

public interface AdminService {

    public void addUser(User user);

    public void updateUser(User user, boolean toHashFlag);

    public void deleteUser(int userId);

    public void addTask(Task task, String username);

    public void updateTask(int taskId, Task updatedTask, String username);

    void deleteTask(int taskId);

    List<User> getAllUsers();

    List<Task> getAllTasks();

    Task findTaskById(int id);
    User findUserById(int id);
}
