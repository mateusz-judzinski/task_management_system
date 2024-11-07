package com.taskmanagement.taskmanagement.service

import com.taskmanagement.taskmanagement.entity.Task
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.repository.TaskRepository
import com.taskmanagement.taskmanagement.repository.UserRepository
import spock.lang.Specification

class TaskServiceSpec extends Specification {

    TaskRepository taskRepository = Mock(TaskRepository)
    UserRepository userRepository = Mock(UserRepository)

    TaskServiceImpl taskService

    def setup() {
        taskService = new TaskServiceImpl(taskRepository, userRepository)
    }

    def "should create a task for the user"(){
        given: "a task and a user"
        def task = new Task(title: "Test task", description: "Description of Test task", priority: 3)
        def user = new User(username: "testUser", email: "testuser@gmail.com")

        userRepository.findByUsername("testUser") >> user

        when: "the task is created"
        taskService.createTask(task, "testUser")

        then: "the task should be added to the user and saved"
        1 * userRepository.save(user)
        user.getTasks().contains(task)
        task.getUser() == user
    }
}
