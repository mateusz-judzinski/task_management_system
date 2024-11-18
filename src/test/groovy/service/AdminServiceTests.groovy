package service

import com.taskmanagement.taskmanagement.entity.Task
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.service.AdminServiceImpl
import com.taskmanagement.taskmanagement.service.TaskService
import com.taskmanagement.taskmanagement.service.UserService
import spock.lang.Specification

class AdminServiceTests extends Specification{

    UserService userService = Mock(UserService)
    TaskService taskService = Mock(TaskService)

    AdminServiceImpl adminService

    def setup(){
        adminService = new AdminServiceImpl(userService, taskService)
    }

    def "should add user by calling register method in UserService"(){
        given: "a user to be add"
        def user = new User(username: "testUser", email: "test@gmail.com")

        when: "method addUser is called"
        adminService.addUser(user)

        then: "method userService.register is called"
        1 * userService.register(user)
    }

    def "should call method updateUser with parameters in UserService"(){
        given: "a user and hash flag"

        def user = new User(username: "testUser", email: "test@gmail.com")
        def toHashFlag = true

        when: "method updateUser is called"
        adminService.updateUser(user, toHashFlag)

        then: "method userService.updateUser is called"
        1 * userService.updateUser(user, toHashFlag)
    }

    def "should call method deleteUserById with userId parameter in UserService"(){
        given: "a users' id"

        def userId = 1

        when: "method deleteUser is called"
        adminService.deleteUser(userId)

        then: "method userService.deleteUserById is called"
        1 * userService.deleteUserById(userId)
    }

    def "should call method createTask with task and username parameters in TaskService"(){
        given: "a task and username"

        def task = new Task(title: "Test Task", description: "Test Description", priority: 3)
        def username = "testUsername"

        when: "method createTask is called"
        adminService.addTask(task, username)

        then: "method taskService.createTask is called"
        1 * taskService.createTask(task, username)
    }

    def "should call method updateTask with taskId, updatedTask and username parameters in TaskService"(){
        given: "a taskId, updatedTask and username"

        def taskId = 1
        def updatedTask = new Task(title: "New Task", description: "New Description", priority: 3)
        def username = "testUsername"

        when: "method updateTask is called"
        adminService.updateTask(taskId, updatedTask, username)

        then: "method taskService.updateTask is called"
        1 * taskService.updateTask(taskId, updatedTask, username)
    }

    def "should call method deleteTask with taskId parameter in TaskService"(){
        given: "a taskId"

        def taskId = 1

        when: "method deleteTask is called"
        adminService.deleteTask(taskId)

        then: "method taskService.deleteTask is called"
        1 * taskService.deleteTask(taskId)
    }

    def "should return a list of users"(){
        given: "userService method"

        def user1 = new User(username: "user1", email: "user1@gmail.com")
        def user2 = new User(username: "user2", email: "user2@gmail.com")
        def usersList = [user1, user2]

        userService.getAllUsers() >> usersList

        when: "method getAllUsers is called"
        def result = adminService.getAllUsers()

        then: "method userService.getAllUsers is called and returned correct list of users"
        result == usersList
        result.size() == 2
        result.contains(user1)
        result.contains(user2)
        result.get(0).username == "user1"
    }

    def "should return empty list of users"(){
        given: "userService method"

        userService.getAllUsers() >> []

        when: "method getAllUsers is called"
        def result = adminService.getAllUsers()

        then: "method userService.getAllUsers is called and returned empty list"
        result.isEmpty()
    }

    def "should return a list of tasks"(){
        given: "getAllTasks method"

        def task1 = new Task(title: "task1", description: "task1 description", priority: 3)
        def task2 = new Task(title: "task2", description: "task2 description", priority: 1)
        def tasksList = [task1, task2]

        taskService.getAllTasks() >> tasksList

        when: "method getAllTasks is called"
        def result = adminService.getAllTasks()

        then: "method taskService.getAllTasks is called and returned correct task list"
        result == tasksList
        result.size() == 2
        result.contains(task1)
        result.contains(task2)
        result.get(1).title == "task2"
    }

    def "should return empty list of tasks"(){
        given: "getAllTasks method"

        taskService.getAllTasks() >> []

        when: "method getAllTasks is called"
        def result = adminService.getAllTasks()

        then: "method taskService.getAllTasks is called and returned empty list"
        result.isEmpty()
    }

    def "should return user by providing correct username parameter"(){
        given: "a valid username"

        def username = "existingUser"
        def user = new User(username: "user", email: "user@gmail.com")

        userService.findUserByUsername(username) >> user

        when: "method findUserByUsername is called"
        def result = adminService.findUserByUsername(username)

        then: "correct user is returned"
        result == user
        result.username == "user"
    }

    def "should return null when invalid username parameter was provided"(){
        given: "a invalid username"

        def username = "nonExistingUser"

        userService.findUserByUsername(username) >> null

        when: "method findUserByUsername is called"
        def result = adminService.findUserByUsername(username)

        then: "method returned null"
        result == null
    }

    def "should return task by providing correct task's id parameter"(){
        given: "a valid task's id"

        def taskId = 1
        def task = new Task(title: "task", description: "task description", priority: 3)

        taskService.findTaskById(taskId) >> task

        when: "method findTaskById is called"
        def result = adminService.findTaskById(taskId)

        then: "correct task is returned"
        result == task
        result.title == "task"
    }

    def "should return null when invalid task's id parameter was provided"(){
        given: "a invalid taskId"

        def taskId = 123

        taskService.findTaskById(taskId) >> null

        when: "method findTaskById is called"
        def result = adminService.findTaskById(taskId)

        then: "method returned null"
        result == null
    }

    def "should return user by providing correct id parameter"(){
        given: "a valid user's id"

        def userId = 1
        def user = new User(username: "user", email: "user@gmail.com")

        userService.findUserById(userId) >> user

        when: "method findUserById is called"
        def result = adminService.findUserById(userId)

        then: "correct user is returned"
        result == user
        result.username == "user"
    }

    def "should return null when invalid id parameter was provided"(){
        given: "a invalid username"

        def userId = 123

        userService.findUserById(userId) >> null

        when: "method findUserById is called"
        def result = adminService.findUserById(userId)

        then: "method returned null"
        result == null
    }

}
