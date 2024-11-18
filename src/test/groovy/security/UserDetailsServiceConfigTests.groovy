package security

import com.taskmanagement.taskmanagement.security.UserDetailsServiceConfig
import spock.lang.Specification
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.provisioning.JdbcUserDetailsManager
import javax.sql.DataSource

class UserDetailsServiceConfigTests extends Specification {

    def "should create UserDetailsManager bean"() {
        given: "UserDetailsServiceConfig instance and a mock DataSource"
        def config = new UserDetailsServiceConfig()
        def dataSource = Mock(DataSource)

        when: "userDetailsManager method is called with the DataSource"
        def userDetailsManager = config.userDetailsManager(dataSource)

        then: "returned object is an instance of JdbcUserDetailsManager"
        userDetailsManager instanceof JdbcUserDetailsManager
    }
}
