package com.taskmanagement.taskmanagement.entity

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import spock.lang.Specification

class UserSpec extends Specification {

    Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def "should validate valid user"() {
        given: "valid user"
        def user = new User(username: "validUser", email: "valid@example.com", password: "Password1")

        when: "using validation on user"
        def violations = validator.validate(user)

        then: "no violations occurs"
        violations.isEmpty()
    }

    def "should fail validation for user with too short username"(){
        given: "user with too short username"
        def user = new User(username: "no", email: "valid@example.com", password: "Password1")

        when: "using validation on user"
        def violations = validator.validate(user)

        then: "one violation occurs for the username"
        violations.size() == 1
        violations.first().message == "Username should be between 3 and 20 characters"
    }

    def "should fail validation for user with invalid password"(){
        given: "user with invalid password"
        def user = new User(username: "validUser", email: "valid@example.com", password: "pas")

        when: "using validation on user"
        def violations = validator.validate(user)

        then: "one violation occurs for the password"
        violations.size() == 2
        violations.first().message == "Password must contain at least one digit, one lowercase letter, one uppercase letter, and be at least 8 characters long"
    }

    def "should fail validation for user with invalid email"(){
        given: "user with invalid email"
        def user = new User(username: "validUser", email: "notAnEmail", password: "Password1")

        when: "using validation on user"
        def violations = validator.validate(user)

        then: "one violation occurs for the email"
        violations.size() == 1
        violations.first().message == "Email should be valid"
    }

    def "should add task to user"() {
        given: "a user and a task"
        def user = new User(username: "validUser", email: "valid@example.com", password: "Password1")
        def task = Mock(Task)

        when: "The task is added to the user"
        user.add(task)

        then: "The user should have one task, and the task's user should be set"
        user.tasks.size() == 1
        user.tasks.contains(task)
        1 * task.setUser(user)
    }
}
