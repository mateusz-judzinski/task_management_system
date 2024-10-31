package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/")
    public String showStartPage(){
        return "home-page";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/process-registration")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if(userService.findUserByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "User already exists");
            return "redirect:/register";
        }

        userService.register(user);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Wrong username or password");
        }
        return "login";
    }


    @PostMapping("/process-login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {
        if (userService.login(username, password)) {
            redirectAttributes.addFlashAttribute("success", "Logged in successfully");
            return "redirect:/tasks";
        } else {
            return "login";
            }
        }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }
}
