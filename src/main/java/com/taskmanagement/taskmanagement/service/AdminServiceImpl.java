package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public AdminServiceImpl(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public void addUser(User user) {
        userService.register(user);
    }

    @Override
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        userService.deleteUserById(userId);
    }

    @Override
    public void addTask(Task task, int userId) {
        taskService.createTask(task, userId);
    }

    @Override
    public void updateTask(int taskId, Task updatedTask, int userId) {
        taskService.updateTask(taskId, updatedTask, userId);
    }

    @Override
    public void deleteTask(int taskId) {
        taskService.deleteTask(taskId);
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    public Task findTaskById(int id) {
        return taskService.findTaskById(id);
    }

    @Override
    public User findUserById(int id) {
        return userService.findUserById(id);
    }

}
