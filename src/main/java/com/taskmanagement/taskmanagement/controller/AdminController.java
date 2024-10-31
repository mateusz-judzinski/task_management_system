package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    public String showAdminLoginForm(){
        return "admin";
    }

    @GetMapping("/user-management")
    public String showUserManagement() {
        return "user-management";
    }

    @GetMapping("/task-management")
    public String showAllTasks(Model model) {
        List<Task> tasks = adminService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "task-management";
    }
}
