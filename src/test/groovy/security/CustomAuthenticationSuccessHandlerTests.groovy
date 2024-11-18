package security

import com.taskmanagement.taskmanagement.security.CustomAuthenticationSuccessHandler
import spock.lang.Specification
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomAuthenticationSuccessHandlerTests extends Specification {

    def "onAuthenticationSuccess should redirect to /tasks for ROLE_USER"() {
        given: "CustomAuthenticationSuccessHandler, mock request, response, and authentication with USER role"
        def handler = new CustomAuthenticationSuccessHandler()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def authentication = Mock(Authentication)

        def authorities = [new SimpleGrantedAuthority("ROLE_USER")]
        authentication.getAuthorities() >> authorities

        when: "onAuthenticationSuccess is called"
        handler.onAuthenticationSuccess(request, response, authentication)

        then: "response should redirect to '/tasks'"
        1 * response.sendRedirect("/tasks")
    }

    def "onAuthenticationSuccess should redirect to /admin for ROLE_ADMIN"() {
        given: "CustomAuthenticationSuccessHandler, mock request, response, and authentication with ADMIN role"
        def handler = new CustomAuthenticationSuccessHandler()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def authentication = Mock(Authentication)

        def authorities = [new SimpleGrantedAuthority("ROLE_ADMIN")]
        authentication.getAuthorities() >> authorities

        when: "onAuthenticationSuccess is called"
        handler.onAuthenticationSuccess(request, response, authentication)

        then: "response should redirect to '/admin'"
        1 * response.sendRedirect("/admin")
    }

    def "onAuthenticationSuccess should redirect to / for unknown roles"() {
        given: "CustomAuthenticationSuccessHandler, mock request, response, and authentication with an unknown role"
        def handler = new CustomAuthenticationSuccessHandler()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def authentication = Mock(Authentication)

        def authorities = [new SimpleGrantedAuthority("ROLE_UNKNOWN")]
        authentication.getAuthorities() >> authorities

        when: "onAuthenticationSuccess is called"
        handler.onAuthenticationSuccess(request, response, authentication)

        then: "response should redirect to '/'"
        1 * response.sendRedirect("/")
    }
}
