package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.User;

import java.util.List;

public interface UserService {

    User findUserByUsername(String username);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUserById(int id);
    void register(User user);
    boolean login (String userName, String password);
    }
