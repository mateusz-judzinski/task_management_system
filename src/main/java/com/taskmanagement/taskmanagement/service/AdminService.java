package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;

import java.util.List;

public interface AdminService {

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(int userId);

    public void addTask(Task task, int userId);

    public void updateTask(int taskId, Task updatedTask);

    void deleteTask(int taskId);

    List<User> getAllUsers();

    List<Task> getAllTasks();
}
