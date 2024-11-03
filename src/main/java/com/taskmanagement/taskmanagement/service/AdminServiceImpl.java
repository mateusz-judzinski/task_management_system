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
    public void updateUser(User user, boolean toHashFlag) {
        userService.updateUser(user, toHashFlag);
    }

    @Override
    public void deleteUser(int userId) {
        userService.deleteUserById(userId);
    }

    @Override
    public void addTask(Task task, String username) {
        taskService.createTask(task, username);
    }

    @Override
    public void updateTask(int taskId, Task updatedTask, String username) {
        taskService.updateTask(taskId, updatedTask, username);
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

    @Override
    public User findUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

    public Task findTaskById(int id) {
        return taskService.findTaskById(id);
    }

    @Override
    public User findUserById(int id) {
        return userService.findUserById(id);
    }

}
