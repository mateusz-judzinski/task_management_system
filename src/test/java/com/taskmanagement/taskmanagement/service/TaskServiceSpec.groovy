package com.taskmanagement.taskmanagement.service

import com.taskmanagement.taskmanagement.entity.Task
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.repository.TaskRepository
import com.taskmanagement.taskmanagement.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

import java.security.Principal

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

        when: "the createTask method i called with valid data"
        taskService.createTask(task, "testUser")

        then: "the task should be added to the user and saved"
        1 * userRepository.save(user)
        user.getTasks().contains(task)
        task.getUser() == user
    }

    def "should throw exception when user is not found"(){
        given: "a task and non existing user"
        def task = new Task(title: "Test task", description: "Description of Test task", priority: 3)
        userRepository.findByUsername("nonExistingUser") >> null

        when: "the task creation is attempted"
        taskService.createTask(task, "nonExistingUser")

        then: "an exception is thrown"
        thrown(NullPointerException)
    }

    def "should return a list of user's tasks by userId"(){
        given: "a userId which refers to user with tasks"

        def userId = 1
        def tasks = [
                new Task(title: "Task 1", description: "Description 1", priority: 1),
                new Task(title: "Task 2", description: "Description 2", priority: 2)
        ]

        taskRepository.findByUserId(userId) >> tasks

        when: "the method getTasksForUser is called with valid data"
        def result = taskService.getTasksForUser(userId)

        then: "the correct list of tasks is returned"
        result == tasks
        result.size() == 2
        result.get(0).getTitle() == "Task 1"
        result.get(1).getTitle() == "Task 2"
    }

    def "should return empty list when calling existing user with no tasks"(){
        given: "a userId which refers to user with no tasks"

        def userId = 2
        taskRepository.findByUserId(userId) >> []

        when: "the method getTasksForUser is called with valid userId"
        def result = taskService.getTasksForUser(userId)

        then: "empty list is returned"
        result.isEmpty()
    }

    def "should return empty list when calling non existing user"(){
        given: "a userId which refers to non existing user"

        def userId = 123
        taskRepository.findByUserId(userId) >> []

        when: "the method getTasksForUser is called with invalid userId"
        def result = taskService.getTasksForUser(userId)

        then: "empty list is returned"
        result.isEmpty()
    }

    def "should update task when provided existing task's id"(){
        given: "updated task and existing task's id"

        def taskId = 1

        def oldTask = new Task(title: "Old Task", description: "Old Description", priority: 1)
        def newTask = new Task(title: "New Task", description: "New Description", priority: 3)

        taskRepository.findById(taskId) >> Optional.of(oldTask)

        when: "the method updateTask is called with valid data"
        taskService.updateTask(taskId, newTask)

        then: "task should be found, updated by new provided one"
        1 * taskRepository.save(oldTask)
        oldTask.title == "New Task"
        oldTask.description == "New Description"
        oldTask.priority == 3
    }

    def "should throw exception when provided non existing task's id"(){
        given: "updated task and non existing task's id"

        def taskId = 123
        def newTask = new Task(title: "New Task", description: "New Description", priority: 3)

        taskRepository.findById(taskId) >> Optional.empty()

        when: "the method updateTask is called with invalid taskId"
        taskService.updateTask(taskId, newTask)

        then: "an exception is thrown"
        def exception = thrown(RuntimeException)
        exception.message == "Task with id: " + taskId + " not found"
    }

    def "should update task when provided existing task's id and existing username"(){
        given: "updated task, existing task's id and existing username"

        def taskId = 1
        def username = "testUser"

        def oldTask = new Task(title: "Old Task", description: "Old Description", priority: 1)
        def newTask = new Task(title: "New Task", description: "New Description", priority: 3)
        def testUser = new User(username: username, email: "test@gmail.com")

        taskRepository.findById(taskId) >> Optional.of(oldTask)
        userRepository.findByUsername(username) >> testUser

        when: "the method updateTask is called with valid data"
        taskService.updateTask(taskId, newTask, username)

        then: "the task should be updated, assigned to the user, and saved"
        1 * taskRepository.save(oldTask)
        oldTask.title == "New Task"
        oldTask.description == "New Description"
        oldTask.priority == 3
        oldTask.user == testUser
    }

    def "should throw exception when providing non existing task's id"(){
        given: "updated task, non existing task's id and valid username"

        def taskId = 123
        def username = "testUser"

        def newTask = new Task(title: "New Task", description: "New Description", priority: 3)

        taskRepository.findById(taskId) >> Optional.empty()

        when: "the method updateTask is called with invalid taskId"
        taskService.updateTask(taskId, newTask, username)

        then: "an exception is thrown"
        def exception = thrown(RuntimeException)
        exception.message == "Task with id: " + taskId + " not found"
    }

    def "should throw exception when providing non existing user's username"(){
        given: "updated task, valid task id and non existing username"

        def taskId = 1
        def username = "nonExistingUsername"

        def oldTask = new Task(title: "Old Task", description: "Old Description", priority: 1)
        def newTask = new Task(title: "New Task", description: "New Description", priority: 3)

        taskRepository.findById(taskId) >> Optional.of(oldTask)
        userRepository.findByUsername(username) >> null

        when: "the method updateTask is called with invalid username"
        taskService.updateTask(taskId, newTask, username)

        then: "an exception is thrown"
        1 * taskRepository.save(oldTask)
        oldTask.title == "New Task"
        oldTask.description == "New Description"
        oldTask.priority == 3
        oldTask.user == null
    }

    def "should delete task by his id"(){
        given: "existing taskId"
        def taskId = 1

        when: "the method updateTask is called with valid taskId"
        taskService.deleteTask(taskId)

        then: "the task should be deleted by calling deleteById with the correct taskId"
        1 * taskRepository.deleteById(taskId)
    }

    def "should handle non existing taskId"() {
        given: "non existing taskId"
        def taskId = 123

        when: "attempting to delete a non existing task"
        taskService.deleteTask(taskId)

        then: "deleteById is still called, but no exception should be thrown"
        1 * taskRepository.deleteById(taskId)
    }

    def "should find and return task by taskId"(){
        given: "an existing taskId"

        def taskId = 1
        def task = new Task(title: "Test Task", description: "Test Description", priority: 3)

        taskRepository.findById(taskId) >> Optional.of(task)

        when: "method findTaskById is called with valid taskId"
        def result = taskService.findTaskById(taskId)

        then: "correct task is returned"
        result == task
        result.title == "Test Task"
    }

    def "should throw exception due to invalid taskId"(){
        given: "not existing taskId"

        def taskId = 123
        taskRepository.findById(taskId) >> Optional.empty()

        when: "method findTaskById is called with invalid taskId"
        taskService.findTaskById(taskId)

        then: "an exception is thrown"
        def exception = thrown(RuntimeException)
        exception.message == "Task with id: " + taskId + " not found"
    }

    def "should return all existing tasks"(){
        given: "repository with multiple tasks"

        def task1 = new Task(title: "Test Task1", description: "Test Description1", priority: 1)
        def task2 = new Task(title: "Test Task2", description: "Test Description2", priority: 2)
        def task3 = new Task(title: "Test Task3", description: "Test Description3", priority: 3)

        def tasks = [task1, task2, task3]

        taskRepository.findAll() >> tasks

        when: "method getAllTasks is called"
        def result = taskService.getAllTasks()

        then: "list of all tasks is returned"
        result == tasks
        result.size() == 3
        result.contains(task1)
        result.contains(task3)
        result.get(1).title == "Test Task2"
    }

    def "should return empty list when no task in repository"(){
        given: "repository with no tasks"

        taskRepository.findAll() >> []

        when: "method getAllTasks is called"
        def result = taskService.getAllTasks()

        then: "empty list is returned"
        result.isEmpty()
    }

    def "should return true when current user is task's owner"(){
        given: "taskId and specific owner"

        def username = "taskOwner"
        def user = new User(username: username)
        def taskId = 1
        def task = new Task(title: "Test Task", user: user)
        def principal = Mock(Principal) {
            getName() >> username
        }

        taskRepository.findById(taskId) >> Optional.of(task)

        when: "checking if the user is the owner of the task"
        def result = taskService.isTaskOwner(taskId, principal)

        then: "the method returns true"
        result
    }

    def "should return false when current user is not task's owner"(){
        given: "taskId and specific user"

        def username = "notTaskOwner"
        def user = new User(username: "taskOwner")
        def taskId = 1
        def task = new Task(title: "Test Task", user: user)
        def principal = Mock(Principal) {
            getName() >> username
        }

        taskRepository.findById(taskId) >> Optional.of(task)

        when: "checking if the user is the owner of the task"
        def result = taskService.isTaskOwner(taskId, principal)

        then: "the method returns false"
        !result
    }

    def "should throw exception when taskId doesn't exist"(){
        given: "non existing taskId and specific user"

        def username = "notTaskOwner"
        def taskId = 123
        def principal = Mock(Principal) {
            getName() >> username
        }

        taskRepository.findById(taskId) >> Optional.empty()

        when: "checking if the user is the owner of the task"
        taskService.isTaskOwner(taskId, principal)

        then: "the method throw exception"
        def exception = thrown(RuntimeException)
        exception.message == "Task with id: " + taskId + " not found"
    }

}
