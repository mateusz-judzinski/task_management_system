package exception

import com.taskmanagement.taskmanagement.controller.TaskController
import com.taskmanagement.taskmanagement.exception.ErrorController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest(ErrorController)
class ErrorControllerTests extends Specification {

    MockMvc mockMvc

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new ErrorController()).build()
    }

    def "should return access-denied view page"() {
        given: "a get request to /access-denied"
        def request = get("/access-denied")

        when: "request is sent to controller"
        def result = mockMvc.perform(request)

        then: "response should return access-denied view page"
        result.andExpect(status().isOk())
                .andExpect(view().name("/exception/access-denied"))
    }
}
