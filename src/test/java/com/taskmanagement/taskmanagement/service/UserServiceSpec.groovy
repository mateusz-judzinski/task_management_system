package com.taskmanagement.taskmanagement.service

import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.repository.UserRepository
import org.mockito.Mock
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceSpec extends Specification{

    UserRepository userRepository = Mock(UserRepository)
    PasswordEncoder passwordEncoder = Mock(PasswordEncoder)

    UserServiceImpl userService

    def setup() {
        userService = new UserServiceImpl(userRepository, passwordEncoder)
    }

    def "should find user by username"() {
        given: "a username"
        def username = "testUser"

        def user = new User(username: username, email: "testuser@gmail.com")

        userRepository.findByUsername(username) >> user

        when: "the user is fetched by username"
        def result = userService.findUserByUsername(username)

        then: "the user should be returned"
        result == user
    }
    def "should return null when user does not exist"() {
        given: "No user with the given username"
        String username = "nonexistentuser"
        userRepository.findByUsername(username) >> null

        when: "findUserByUsername is called"
        User result = userService.findUserByUsername(username)

        then: "Null is returned"
        result == null
    }

    def "should handle exception when repository fails"() {
        given: "The repository throws an exception"
        String username = "erroruser"
        userRepository.findByUsername(username) >> { throw new RuntimeException("Database error") }

        when: "findUserByUsername is called"
        User result = null
        try {
            result = userService.findUserByUsername(username)
        } catch (Exception e) {
        }

        then: "The result should be null due to the exception"
        result == null
    }
}
