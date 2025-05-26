package com.antmen.antwork.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder makePassword(){
        log.info("makePassword()");
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
