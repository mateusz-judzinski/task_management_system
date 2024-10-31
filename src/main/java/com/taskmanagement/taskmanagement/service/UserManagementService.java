package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;

import java.util.List;

public interface UserManagementService {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int userId);

    void addTask(Task task, int userId);

    void updateTask(int taskId, Task updatedTask);

    void deleteTask(int taskId);

    List<User> getAllUsers();

    List<Task> getAllTasks();
}
