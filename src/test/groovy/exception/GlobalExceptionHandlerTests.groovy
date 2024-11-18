package exception

import com.taskmanagement.taskmanagement.controller.AdminController
import com.taskmanagement.taskmanagement.exception.GlobalExceptionHandler
import jakarta.persistence.EntityNotFoundException
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.resource.NoResourceFoundException
import spock.lang.Specification

import java.util.NoSuchElementException

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest(GlobalExceptionHandler)
class GlobalExceptionHandlerTests extends Specification {

    MockMvc mockMvc

    @RestController
    static class TestController {

        @GetMapping("/test/no-such-element")
        static String throwNoSuchElementException() {
            throw new NoSuchElementException("Test NoSuchElementException")
        }

        @GetMapping("/test/entity-not-found")
        static String throwEntityNotFoundException() {
            throw new EntityNotFoundException("Test EntityNotFoundException")
        }

        @GetMapping("/test/illegal-argument")
        static String throwIllegalArgumentException() {
            throw new IllegalArgumentException("Test IllegalArgumentException")
        }

        @GetMapping("/test/no-resource-found")
        static String throwNoResourceFoundException() {
            throw new NoResourceFoundException(null, null)
        }
    }

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build()
    }

    def "should return error-page and error message for NoSuchElementException"() {
        given: "request to an endpoint"
        def request = get("/test/no-such-element")

        when: "request is sent to endpoint that throws NoSuchElementException"
        def result = mockMvc.perform(request)

        then: "response should return error-page with specific error message"
        result.andExpect(status().isOk())
                .andExpect(view().name("exception/error-page"))
                .andExpect(model().attribute("errorMessage", "The requested task was not found."))
    }

    def "should return error-page and error message for EntityNotFoundException"() {
        given: "request to an endpoint"
        def request = get("/test/entity-not-found")

        when: "request is sent to endpoint that throws EntityNotFoundException"
        def result = mockMvc.perform(request)

        then: "response should return error-page with specific error message"
        result.andExpect(status().isOk())
                .andExpect(view().name("exception/error-page"))
                .andExpect(model().attribute("errorMessage", "The entity you are trying to access does not exist."))
    }

    def "should return error-page and error message for IllegalArgumentException"() {
        given: "request to an endpoint"
        def request = get("/test/illegal-argument")

        when: "request is sent to endpoint that throws IllegalArgumentException"
        def result = mockMvc.perform(request)

        then: "response should return error-page with specific error message"
        result.andExpect(status().isOk())
                .andExpect(view().name("exception/error-page"))
                .andExpect(model().attribute("errorMessage", "Invalid input provided."))
    }

    def "should return error-page and error message for NoResourceFoundException"() {
        given: "request to an endpoint"
        def request = get("/test/no-resource-found")

        when: "request is sent to endpoint that throws NoResourceFoundException"
        def result = mockMvc.perform(request)

        then: "response should return error-page with specific error message"
        result.andExpect(status().isOk())
                .andExpect(view().name("exception/error-page"))
                .andExpect(model().attribute("errorMessage", "The requested resource could not be found."))
    }
}
