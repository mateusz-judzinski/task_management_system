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

    def "should return all user"() {
        given: "repository with multiple users"
        def users = [
                new User(username: "user1", email: "user1@gmail.com"),
                new User(username: "user2", email: "user2@gmail.com")
        ]
        userRepository.findAll() >> users

        when: "getAllUsers is called"
        def result = userService.getAllUsers()

        then: "the correct list of users is returned"
        result == users
    }

    def "should update user and hash his password when toHashFlag is true"(){
        given: "an existing, updated user and new (unhashed) password to hash"
        def user = new User(username: "testUser", password: "newPassword", email: "testEmail@gmail.com")

        def oldPassword = user.getPassword()
        def hashedPassword = "hashedPassword"

        passwordEncoder.encode(user.getPassword()) >> hashedPassword
        passwordEncoder.matches(user.getPassword(), hashedPassword) >> true

        when: "updateUser is called with toHashFlag true"
        userService.updateUser(user, true)

        then: "the user is updated and his password is hashed"
        user.password == hashedPassword
        passwordEncoder.matches(oldPassword, hashedPassword)
        user.username == "testUser"
        user.email == "testEmail@gmail.com"
    }

    def "should update user and without hashing his password when toHashFlag is false"(){
        given: "an existing, updates user and old (hashed) password"
        def user = new User(username: "testUser", password: "oldHashedPassword", email: "testEmail@gmail.com")

        when: "updateUser is called with toHashFlag false"
        userService.updateUser(user, false)

        then: "the user is updated and his password still the same"
        user.password == "oldHashedPassword"
        user.email == "testEmail@gmail.com"
        user.username == "testUser"
    }

    def "should delete user by id"(){
        given: "a user id"
        def userId = 1

        when: "deleteUserById is called"
        userService.deleteUserById(userId)

        then: "the repository deleteById method is called"
        1 * userRepository.deleteById(userId)
    }

    def "should register user, hash his password and set the role on 'ROLE_USER'"(){
        given: "a user with plain password"
        def user = new User(username: "testUser", password: "testPassword", email: "testUser@gmail.com")

        def hashedPassword = "hashedPassword"
        passwordEncoder.encode(user.password) >> hashedPassword

        when: "register method is called"
        userService.register(user)

        then: "user is registered with encoded password and role 'ROLE_USER'"
        user.role == "ROLE_USER"
        user.password == hashedPassword
    }

    def "should return true when password matches"(){
        given: "correct username and password"
        def username = "validUsername"
        def password = "validPassword"

        def user = new User(username: username, password: "hashedPassword")

        passwordEncoder.matches(password, user.password) >> true
        userRepository.findByUsername(username) >> user

        when: "login is called with correct credentials"
        def result = userService.login(username, password)

        then: "the result is true"
        result
    }

    def "should return false when password does not match"(){
        given: "username and an incorrect password"
        def username = "validUsername"
        def password = "invalidPassword"

        def user = new User(username: username, password: "hashedPassword")

        passwordEncoder.matches(password, user.password) >> false
        userRepository.findByUsername(username) >> user

        when: "login is called with incorrect password"
        def result = userService.login(username, password)

        then: "the result is false"
        !result
    }

    def "should return false when user does not exist"(){
        given: "username that not exist and password"
        def username = "nonExistingUsername"

        userRepository.findByUsername(username) >> null

        when: "login is called with non existing username"
        def result = userService.login(username, "password")

        then: "the result is false"
        !result
    }

    def "should find and return existing user by id"(){
        given: "an existing user id"

        def userId = 1
        def user = new User (id: userId, username: "testUser", email: "testUser@gmail.com")

        userRepository.findById(userId) >> Optional.of(user)

        when: "findUserById is called with existing id"
        def result = userService.findUserById(userId)

        then: "user is found and returned"
        result == user
    }

    def "should throw exception when user does not exist"(){
        given: "a non existing user id"

        def userId = 1
        userRepository.findById(userId) >> Optional.empty()

        when: "findUserById is called with non existing user id"
        def result = null
        try {
            result = userService.findUserById(userId)
        } catch (Exception e) {
            result = e
        }

        then: "an exception is thrown"
        result instanceof RuntimeException
    }

}
