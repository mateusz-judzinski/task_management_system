package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.TaskRepository;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void createTask(Task task, int userId) {

        User tempUser = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User with id: " + userId + " not found"));

        tempUser.add(task);

        userRepository.save(tempUser);
    }

    @Override
    public List<Task> getTasksForUser(int userId) {

        return taskRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void updateTask(int taskId, Task updatedTask) {

        Task tempTask = taskRepository.findById(taskId).orElseThrow(() ->
                new RuntimeException("Task with id: " + taskId + " not found"));

        tempTask.setTitle(updatedTask.getTitle());
        tempTask.setDescription(updatedTask.getDescription());
        tempTask.setPriority(updatedTask.getPriority());

        taskRepository.save(tempTask);

    }

    @Transactional
    @Override
    public void deleteTask(int taskId) {

        taskRepository.deleteById(taskId);
    }

    @Override
    public Task findTaskById(int taskId) {

        return taskRepository.findById(taskId).orElseThrow(() ->
                new RuntimeException("Task with id: " + taskId + " not found"));
    }

    @Override
    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    @Override
    public boolean isTaskOwner(int taskId, Principal principal) {

        Task tempTask = taskRepository.findById(taskId).orElseThrow(() ->
                new RuntimeException("Task with id: " + taskId + " not found"));

        String username = principal.getName();
        if(tempTask.getUser().getUsername().equals(username)){
            return true;
        }
        return false;
    }


}
