package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.service.TaskService;
import com.taskmanagement.taskmanagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String getUserTasks(Model model, Principal principal){

        String tempUsername = principal.getName();

        User tempUser = userService.findUserByUsername(tempUsername);

        int tempId = tempUser.getId();

        List<Task> tasks = taskService.getTasksForUser(tempId);

        model.addAttribute("tasks", tasks);

        return "tasks";
    }



}
