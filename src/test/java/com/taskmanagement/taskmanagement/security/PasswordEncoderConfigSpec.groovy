package com.taskmanagement.taskmanagement.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@SpringBootTest
class PasswordEncoderConfigSpec extends Specification{

    PasswordEncoder passwordEncoder

    def setup() {
        passwordEncoder = new BCryptPasswordEncoder()
    }

    def "should encode and match password"(){
        given: "plain password"
        def rawPassword = "testPassword"

        when: "password is encoded"
        def encodedPassword = passwordEncoder.encode(rawPassword)

        then: "encoded password should match with raw password"
        passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
