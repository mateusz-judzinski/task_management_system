package com.taskmanagement.taskmanagement.service

import com.taskmanagement.taskmanagement.entity.User
import spock.lang.Specification

class AdminServiceSpec extends Specification{

    UserService userService = Mock(UserService)
    TaskService taskService = Mock(TaskService)

    AdminServiceImpl adminService

    def setup(){
        adminService = new AdminServiceImpl(userService, taskService)
    }

    def "should add user to by calling register method in UserService"(){
        given: "a user to be add"
        def user = new User(username: "testUser", email: "test@gmail.com")


    }
}
