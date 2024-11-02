package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "admin/admin";
    }

    @GetMapping("/users")
    public String showUserManagement(Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin-all-users";
    }

    @GetMapping("/user/new")
    public String addNewUser(Model model){
        model.addAttribute("user", new User());

        return "admin/admin-user-form";
    }

    @PostMapping("/user")
    public String saveNewUser(@ModelAttribute("user") User user,
                              BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "admin/admin-user-form";
        }

        adminService.addUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int userId, Model model) {
        model.addAttribute("user", adminService.findUserById(userId));
        return "admin/admin-user-form";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") User user,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "admin/admin-user-form";
        }

        if(user.getNewPassword() != null && !user.getNewPassword().isBlank()){
            user.setPassword(user.getNewPassword());
        }

        adminService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId){
        adminService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/tasks")
    public String showAllTasks(Model model) {
        List<Task> tasks = adminService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "admin/admin-all-tasks";
    }

    @GetMapping("/task/new")
    public String addNewTask(Model model){
        model.addAttribute("task", new Task());

        return "admin/admin-task-form";
    }

    @PostMapping("/task")
    public String saveNewTask(@ModelAttribute("task") @Valid Task task,
                              @RequestParam("userId") int userId,
                              BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "admin/admin-task-form";
        }

        adminService.addTask(task, userId);
        return "redirect:/admin/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") int taskId, Model model) {
        model.addAttribute("task", adminService.findTaskById(taskId));
        return "admin/admin-task-form";
    }

    @PostMapping("/task/update")
    public String updateTask(@ModelAttribute("task") @Valid Task task,
                             @RequestParam("userId") int userId,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "admin/admin-task-form";
        }

        adminService.updateTask(task.getId(), task, userId);
        return "redirect:/admin/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable("id") int taskId){
        adminService.deleteTask(taskId);
        return "redirect:/admin/tasks";
    }
}
