package com.taskmanagement.taskmanagement.controller

import com.taskmanagement.taskmanagement.controller.UserController
import com.taskmanagement.taskmanagement.entity.User
import com.taskmanagement.taskmanagement.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


@WebMvcTest(UserController)
class UserControllerSpec extends Specification {

    MockMvc mockMvc
    UserService userService = Mock(UserService)

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build()
    }

    def "should return home page view on root URL"() {
        given: "root URL request to the controller"
        def request = get("/")

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response status should be OK (200)"
        result.andExpect(status().isOk())

        and: "view should be the home page template 'user/home-page'"
        result.andExpect(view().name("user/home-page"))
    }

    def "should return register page view with new user attribute in model"() {
        given: "/register URL request to the controller"
        def request = get("/register")

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response status should be OK (200)"
        result.andExpect(status().isOk())

        and: "view should be the register page template 'user/register'"
        result.andExpect(view().name("user/register"))

        and: "model should contain an attribute 'user' with an empty User instance"
        result.andExpect(model().attributeExists("user"))
        def userAttribute = result.andReturn().modelAndView.model.get("user")
        userAttribute instanceof User
    }

    def "should return register page view when validation error exist"(){
        given: "a user with validation error"
        def user = new User(username: "", password: "", email: "")
        def request =
                post("/process-registration").flashAttr("user", user)

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should return to the register page view due to validation error"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/register"))
    }

    def "should redirect to /register with error if user already exists"() {
        given: "valid user with an existing username"
        def user = new User(username: "existingUser", password: "Password123", email: "email@example.com")
        def request =
                post("/process-registration").flashAttr("user", user)

        and: "userService returns an existing user for the username"
        userService.findUserByUsername(user.username) >> user

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should redirect to /register with an error message"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute("error", "User already exists"))
    }

    def "should register user and redirect to /login on successful registration"(){
        given: "valid user with username that doesn't exist in database"
        def user = new User(username: "uniqueUser", password: "Password123", email: "email@example.com")
        def request =
                post("/process-registration").flashAttr("user", user)

        and: "userService returns null due to unique username"
        userService.findUserByUsername(user.username) >> null

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should redirect to /login and correctly register user"
        1 * userService.register(user)
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
    }

    def "should return login page view when no error param is passed"(){
        given: "request to /login"
        def request = get("/login")

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should return /user/login view page without error"
        result.andExpect(view().name("user/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
    }

    def "should return login page view with an error param and message"(){
        given: "request to /login"
        def request = get("/login").param("error", "true")

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should return /user/login with an error param"
        result.andExpect(view().name("user/login"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Wrong username or password"))
    }

    def "should redirect to user '/tasks' page view if correct credentials provided"(){
        given: "valid username, password and request to /process-login"

        def username = "validUsername"
        def password = "validPassword"
        def request =
                post("/process-login")
                .param("username", username)
                .param("password", password)

        userService.login(username, password) >> true

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should redirect to /tasks page"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
    }

    def "should return user/login page view if invalid credentials"(){
        given: "invalid username, password and request to /process-login"

        def username = "invalidUsername"
        def password = "invalidPassword"
        def request =
                post("/process-login")
                        .param("username", username)
                        .param("password", password)

        userService.login(username, password) >> false

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should redirect to /tasks page"
        result.andExpect(status().isOk())
                .andExpect(view().name("user/login"))
    }

    def "should redirect to /login page and logout from the account"(){
        given: "authenticated user and request to /logout"

        def request = post("/logout")
        def response = Mock(HttpServletResponse)
        def authentication = Mock(Authentication)
        def securityContextLogoutHandler = Mock(SecurityContextLogoutHandler)

        and: "securityContextLogoutHandler.logout() is called"
        securityContextLogoutHandler.logout(request, response, authentication) >> null

        when: "request is sent to the UserController"
        def result = mockMvc.perform(request)

        then: "response should redirect to /login"
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
    }
}
