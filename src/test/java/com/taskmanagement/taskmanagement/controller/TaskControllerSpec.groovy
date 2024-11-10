package com.taskmanagement.taskmanagement.controller

import com.taskmanagement.taskmanagement.entity.Task
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.service.TaskService
import com.taskmanagement.taskmanagement.service.UserService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.BindingResult
import spock.lang.Specification

import java.security.Principal

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class TaskControllerSpec extends Specification{

    MockMvc mockMvc
    TaskService taskService = Mock(TaskService)
    UserService userService = Mock(UserService)

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService, userService)).build()
    }

    def "should return tasks page view with user's tasks"() {
        given: "authenticated user with username, id and request"
        def request = get("/tasks")
        def username = "testUser"
        def userId = 1
        def principal = Mock(Principal)
        principal.getName() >> username

        and: "a user is found by username and has an id"
        def user = new User(id: userId, username: username)
        userService.findUserByUsername(username) >> user

        and: "list of user's tasks"
        def tasks = [
                new Task(title: "task1", description: "description1", priority: 1),
                new Task(title: "task2", description: "description2", priority: 2),
                new Task(title: "task3", description: "description3", priority: 3)
        ]
        taskService.getTasksForUser(userId) >> tasks

        when: "request is sent to /tasks"
        def result = mockMvc.perform(request.principal(principal))

        then: "response status should be OK (200)"
        result.andExpect(status().isOk())

        and: "view should be tasks page template 'user/tasks'"
        result.andExpect(view().name("user/tasks"))

        and: "model should contain a list of tasks"
        result.andExpect(model().attribute("tasks", tasks))
    }

    def "should return task form view and model with new task attribute"(){
        given: "request to /tasks/new"
        def request = get("/tasks/new")

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "view should be task-form page template and status is OK (200)"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/task-form"))

        and: "model should contain a new Task"
        result.andExpect(model().attributeExists("task"))

        def taskAttribute = result.andReturn().modelAndView.model.get("task")
        taskAttribute instanceof Task
    }

    def "should save task and redirect to /tasks if there are no validation errors"(){
        given: "valid task, logged in user and post request"
        def request = post("/tasks")

        def task = new Task(title: "Valid Task", description: "Valid Description", priority: 3)
        def username = "testUser"

        def principal = Mock(Principal)
        principal.getName() >> username

        and: "no validation errors"
        def bindingResult = Mock(BindingResult)
        bindingResult.hasErrors() >> false

        when: "post request sent to controller"
        def result =
                mockMvc.perform(request
                        .flashAttr("task", task)
                        .principal(principal))

        then: "task should be saved"
        1 * taskService.createTask(task, username)

        and: "response should redirect to /tasks page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
    }

    def "should return to task-form page view if there is validation error"(){
        given: "invalid task (too short title), logged in user and post request"
        def request = post("/tasks")

        def task = new Task(title: "", description: "", priority: 10)
        def username = "testUser"

        def principal = Mock(Principal)
        principal.getName() >> username

        when: "request is sent to controller"
        def result = mockMvc.perform(request
                .flashAttr("task", task)
                .principal(principal))

        then: "response should return to task-form page view due to validation error"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/task-form"))
                .andExpect(model().attributeHasFieldErrors("task", "title"))
                .andExpect(model().attributeHasFieldErrors("task", "priority"))
    }

    def "should return edit task-form for specified task if user is task's owner"(){
        given: "get request to /edit/{taskId}, taskId and authenticated user"
        def taskId = 1
        def request = get("/tasks/edit/{taskId}", taskId)

        def principal = Mock(Principal)

        def task = new Task(title: "Test Task", description: "Test Description", priority: 3)
        taskService.findTaskById(taskId) >> task
        taskService.isTaskOwner(taskId, principal) >> true

        when: "request is sent to Controller"
        def result = mockMvc.perform(request.principal(principal))

        then: "response should be OK (200) and view 'user/task-form' returned"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/task-form"))

        and: "model should contain task under 'task' attribute"
        result.andExpect(model().attribute("task", task))
    }

    def "should redirect to access-denied page if user isn't task's owner"(){
        given: "get request to /edit/{taskId}, taskId and authenticated user who's not task owner"
        def taskId = 1
        def request = get("/tasks/edit/{taskId}", taskId)

        def principal = Mock(Principal)

        def task = new Task(title: "Test Task", description: "Test Description", priority: 3)
        taskService.findTaskById(taskId) >> task
        taskService.isTaskOwner(taskId, principal) >> false

        when: "request is sent to controller"
        def result = mockMvc.perform(request.principal(principal))

        then: "response should redirect to /access-denied page view"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access-denied"))
    }

    def "should update task if user is task's owner and no validation errors"(){
        given: "post request, taskId, and authenticated user"
        def taskId = 1
        def request = post("/tasks/update/{taskId}", taskId)
        def principal = Mock(Principal)

        taskService.isTaskOwner(taskId, principal) >> true

        when: "request is sent to controller with valid params"
        def result = mockMvc.perform(request
                .param("title", "Updated Task")
                .param("description", "Updated Description")
                .param("priority", "2")
                .principal(principal))

        then: "response should redirect to '/tasks"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
    }

    def "should return to task-form page view if task have invalid data"(){
        given: "post request, taskId, and authenticated user"
        def taskId = 1
        def request = post("/tasks/update/{taskId}", taskId)
        def principal = Mock(Principal)

        taskService.isTaskOwner(taskId, principal) >> true

        when: "request is sent to controller with invalid params"
        def result = mockMvc.perform(request
                .param("title", "")
                .param("description", "")
                .param("priority", "30")
                .principal(principal))

        then: "response should return task-form page due to validation error"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/task-form"))

        and: "model should contain errors: title, description and priority"
        result.andExpect(model().attributeHasFieldErrors("task", "title"))
                .andExpect(model().attributeHasFieldErrors("task", "description"))
                .andExpect(model().attributeHasFieldErrors("task", "priority"))
    }

    def "should redirect to access-denied page view if user is not task's owner"(){
        given: "post request, taskId and authenticated user who's not task's owner"
        def taskId = 1
        def request = post("/tasks/update/{taskId}", taskId)
        def principal = Mock(Principal)

        taskService.isTaskOwner(taskId, principal) >> false

        when: "request is sent to controller"
        def result = mockMvc.perform(request
                .param("title", "Updated Task")
                .param("description", "Updated Description")
                .param("priority", "2")
                .principal(principal))

        then: "response should redirect to access-denied page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access-denied"))
    }

    def "should delete task if user is task's owner and redirect to /tasks"(){
        given: "get request, taskId and authenticated user"
        def taskId = 1
        def request = get("/tasks/delete/{taskId}", taskId)
        def principal = Mock(Principal)

        taskService.isTaskOwner(taskId, principal) >> true

        when: "request is sent to controller"
        def result = mockMvc.perform(request.principal(principal))

        then: "response should redirect to /tasks page and call deleteTask method"
        1 * taskService.deleteTask(taskId)
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
    }

    def "should redirect to /access-denied page if user is not task's owner"(){
        given: "get request, taskId and authenticated user who's not task's owner"
        def taskId = 1
        def request = get("/tasks/delete/{taskId}", taskId)
        def principal = Mock(Principal)

        taskService.isTaskOwner(taskId, principal) >> false

        when: "request is sent to controller"
        def result = mockMvc.perform(request.principal(principal))

        then: "response should redirect to access-denied page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access-denied"))
    }
}