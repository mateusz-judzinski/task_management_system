package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model){

        if(userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "User already exists");
            return "register";
        }
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){

        return "login";
    }

    @PostMapping("/process-login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            if (userService.login(username, password)) {
                redirectAttributes.addFlashAttribute("success", "Logged in successfully");
                return "redirect:/";
            } else {
                model.addAttribute("error", "Wrong username or password");
            }
        } else {
            model.addAttribute("error", "User not found");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(Model model){

        return "dashboard";
    }
}
