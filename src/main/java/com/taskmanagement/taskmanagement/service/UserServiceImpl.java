package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(User user) {

        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void register(User user) {

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    @Override
    public boolean login(String userName, String password) {

        User user = userRepository.findByUsername(userName);
        if(user != null){

            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

}
