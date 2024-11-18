package entity

import com.taskmanagement.taskmanagement.entity.Task
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import spock.lang.Specification

class TaskTests extends Specification{

    Validator validator

    def setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def "should validate valid task"(){
        given: "valid task"
        def task = new Task(title: "Valid Task", description: "Valid Description", priority: 4)

        when: "using validation on task"
        def violations = validator.validate(task)

        then: "no violations occurs"
        violations.isEmpty()
    }

    def "should fail validation for task with too short title"(){
        given: "task with too short title"
        def task = new Task(title: "no", description: "Valid Description", priority: 4)

        when: "using validation on task"
        def violations = validator.validate(task)

        then: "one violation occurs for title"
        violations.size() == 1
        violations.first().message == "Task title should be between 3 and 50 characters"
    }

    def "should fail validation for task with blank description"(){
        given: "task with blank description"
        def task = new Task(title: "Valid Task", description: "   ", priority: 4)

        when: "using validation on task"
        def violations = validator.validate(task)

        then: "one violation occurs for description"
        violations.size() == 1
        violations.first().message == "Task description cannot be blank"
    }

    def "should fail validation for task with priority out of scale 1 to 5"(){
        given: "task with wrong priority"
        def task = new Task(title: "Valid Task", description: "Valid Description", priority: 123)

        when: "using validation on task"
        def violations = validator.validate(task)

        then: "one violation occurs for priority"
        violations.size() == 1
    }
}