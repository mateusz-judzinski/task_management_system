package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login-admin")
    public String showAdminLoginForm(){
        return "login-admin";
    }



}
