package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Admin;
import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.AdminRepository;

import java.util.List;

public interface AdminService {

    void addAdmin(Admin admin);
    void deleteAdminById(int id);
    List<Admin> getAllAdmins();
    Admin findAdminByUsername(String username);

}
