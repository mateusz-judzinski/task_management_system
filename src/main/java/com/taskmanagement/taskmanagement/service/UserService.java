package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.User;

import java.util.List;

public interface UserService {

    void registerUser(User user);
    User findUserByUsername(String username);
    List<User> getAllUsers();
}
