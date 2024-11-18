package controller

import com.taskmanagement.taskmanagement.controller.AdminController
import com.taskmanagement.taskmanagement.entity.Task
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.service.AdminService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(AdminController)
class AdminControllerTests extends Specification{

    MockMvc mockMvc
    AdminService adminService = Mock(AdminService)

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(adminService)).build()
    }

    def "should return admin homepage"(){
        given: "get request to /admin"
        def request = get("/admin")

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin/admin view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin"))
    }

    def "should return admin-all-users page with list of users"(){
        given: "get request to /admin/users"
        def request = get("/admin/users")

        and: "list of users"
        def usersList = [
                new User(username: "testUser1", email: "testEmail1@gmail.com"),
                new User(username: "testUser2", email: "testEmail2@gmail.com"),
                new User(username: "testUser3", email: "testEmail3@gmail.com")
        ]
        adminService.getAllUsers() >> usersList

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return to admin-all-user view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-all-users"))

        and: "model should contain usersList"
        result.andExpect(model().attribute("users", usersList))
    }

    def "should return admin-new-user-form page with user attribute"(){
        given: "get request to /admin/user/new"
        def request = get("/admin/user/new")

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin-new-user-form view page and new User model attribute"
        def userAttribute = result.andReturn().modelAndView.model.get("user")
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-new-user-form"))
                .andExpect(model().attributeExists("user"))
        userAttribute instanceof User
    }

    def "should save new user and redirect to /admin/users page view if valid data and unique username"(){
        given: "post request to /admin/user"
        def request = post("/admin/user")

        def username = "testUser"
        adminService.findUserByUsername(username) >> null

        when: "request is sent to controller with valid data"
        def result = mockMvc.perform(request
                .param("username", username)
                .param("password", "Password1")
                .param("email", "validEmail@gmail.com"))

        then: "response should redirect to /admin/users page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
    }

    def "should return back to admin-new-user-form if user data invalid"(){
        given: "post request to /admin/user"
        def request = post("/admin/user")

        when: "request is sent to controller with invalid data"
        def result = mockMvc.perform(request
                .param("username", "")
                .param("password", "")
                .param("email", ""))

        then: "response should return back to /admin-new-user-form due to invalid data"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-new-user-form"))
                .andExpect(model().attributeHasFieldErrors("user", "username"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
    }

    def "should redirect back to /admin/user/new URL page if username isn't unique"(){
        given: "post request to /admin/user"
        def request = post("/admin/user")

        def username = "existingUsername"
        def user = new User(username: username, password: "Password1", email: "validEmail@gmail.com")
        adminService.findUserByUsername(username) >> user

        when: "request is sent to controller with already existing username"
        def result = mockMvc.perform(request
                .param("username", user.username)
                .param("password", user.password)
                .param("email", user.email))

        then: "response should redirect back to /admin/user/new page with error attribute"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/new"))
                .andExpect(flash().attribute("error", "User already exists"))
    }

    def "should return admin-edit-user-form view page with user (found by id) in model attribute"(){
        given: "get request and userId"
        def userId = 1
        def request = get("/admin/user/edit/{id}", userId)

        def username = "tempUser"
        def user = new User(username: username)
        adminService.findUserById(userId) >> user

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin-edit-user-form view page with user instance in attribute"
        def userAttribute = result.andReturn().modelAndView.model.get("user")
        userAttribute instanceof User
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-edit-user-form"))
                .andExpect(model().attribute("user", user))
    }

    def "should update user and redirect to /admin/users if user's data valid"(){
        given: "post request"
        def request = post("/admin/user/update")

        when: "request is sent to controller with valid data"
        def result = mockMvc.perform(request
                .param("username", "validUsername")
                .param("newPassword", "")
                .param("password", "Password1")
                .param("email", "validEmail@gmail.com"))

        then: "response should redirect to /admin/user url page with user in model attribute"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(model().attributeExists())
    }

    def "should return back to /admin-edit-user-form view page if user data invalid"(){
        given: "post request"
        def request = post("/admin/user/update")

        when: "request is sent to controller with valid data"
        def result = mockMvc.perform(request
                .param("username", "")
                .param("newPassword", "")
                .param("password", "")
                .param("email", ""))

        then: "response should return back to /admin-edit-user-form view page due to invalid user data"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-edit-user-form"))
    }

    def "should delete user by provided id and redirect to /admin/users URL page"(){
        given: "get request and userId"
        def userId = 1
        def request = get("/admin/user/delete/{id}", userId)

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should redirect to /admin/users URL page and call deleteUser method"
        1 * adminService.deleteUser(userId)
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
    }

    def "should return /admin-all-tasks view page with all tasks in model attribute"(){
        given: "get request and tasks list"
        def request = get("/admin/tasks")
        def tasksList = [
                new Task(title: "test1", description: "test description1", priority: 1),
                new Task(title: "test2", description: "test description2", priority: 2),
                new Task(title: "test3", description: "test description3", priority: 3)
        ]
        adminService.getAllTasks() >> tasksList

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin-all-tasks view page and tasks in model attribute"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-all-tasks"))
                .andExpect(model().attribute("tasks", tasksList))
    }

    def "should return admin-task-form with list of user's names having role 'ROLE_USER'"(){
        given: "get request and list of user's names"
        def request = get("/admin/task/new")

        def user1 = new User(username: "user1", role: "ROLE_USER")
        def user2 = new User(username: "user2", role: "ROLE_USER")
        def admin = new User(username: "admin", role: "ROLE_ADMIN")

        adminService.getAllUsers() >> [user1 , user2, admin]

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin-task-form view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-task-form"))

        and: "user's list with role user and task in model attribute"
        result.andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("users", ["user1", "user2"]))
    }

    def "should save task and redirect to /admin/tasks URL page if task have valid data"(){
        given: "post request"
        def request = post("/admin/task")

        when: "request is sent to controller"
        def result = mockMvc.perform(request
                .param("title", "Valid Title")
                .param("description", "Valid Description")
                .param("priority", "1")
                .param("username", "testUser"))

        then: "response should redirect to /admin/tasks URL page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks"))
    }

    def "should return -admin-task-form back again due to invalid task data"(){
        given: "post request"
        def request = post("/admin/task")

        when: "request is sent to controller"
        def result = mockMvc.perform(request
                .param("title", "")
                .param("description", "")
                .param("priority", "10")
                .param("username", "username"))

        then: "response should return admin-task-form view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-task-form"))

        and: "model contains error on fields: title, description, priority"
        result.andExpect(model().attributeHasFieldErrors("task", "title"))
                .andExpect(model().attributeHasFieldErrors("task", "description"))
                .andExpect(model().attributeHasFieldErrors("task", "priority"))
    }

    def "should return /admin-task-form view page and parse user's names list with role user"(){
        given: "get request, list of user's names and task id"
        def taskId = 1
        def request = get("/admin/task/edit/{id}", taskId)

        def task = new Task(id: taskId, title: "Test Task", description: "Test Description", priority: 1)
        adminService.findTaskById(taskId) >> task

        def user1 = new User(username: "user1", role: "ROLE_USER")
        def user2 = new User(username: "user2", role: "ROLE_USER")
        def admin = new User(username: "admin", role: "ROLE_ADMIN")
        adminService.getAllUsers() >> [user1 , user2, admin]

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return admin-task-form view page with task and user list"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-task-form"))

        and: "user's list with role user and task in model attribute"
        def taskAttribute = result.andReturn().modelAndView.model.get("task")
        taskAttribute instanceof Task
        result.andExpect(model().attribute("task", task))
                .andExpect(model().attribute("users", ["user1", "user2"]))
    }

    def "should update task and redirect to /admin/tasks URL page if task have valid data"(){
        given: "post request, username"
        def request = post("/admin/task/update")
        def username = "testUser"

        when: "request is sent to controller with valid task's data"
        def result = mockMvc.perform(request
                .param("title", "Valid Title")
                .param("description", "Valid Description")
                .param("priority", "3")
                .param("username", username))

        then: "response should redirect to /admin/tasks"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks"))
    }

    def "should return /admin-task-form view page due to task's invalid data"(){
        given: "post request, username"
        def request = post("/admin/task/update")
        def username = "testUser"

        when: "request is sent to controller with invalid task's data"
        def result = mockMvc.perform(request
                .param("title", "")
                .param("description", "")
                .param("priority", "")
                .param("username", username))

        then: "response should return back to /admin-task-form view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("admin/admin-task-form"))
    }

    def "should delete task and redirect to /admin/tasks URL page"(){
        given: "get request and taskId"
        def taskId = 1
        def request = get("/admin/task/delete/{id}", taskId)

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should redirect to /admin/tasks URL page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tasks"))

        and: "call on adminService.deleteTask once"
        1 * adminService.deleteTask(taskId)
    }
}