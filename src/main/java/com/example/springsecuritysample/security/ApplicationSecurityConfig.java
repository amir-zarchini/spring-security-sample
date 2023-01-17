package com.example.springsecuritysample.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig{

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/*", "index", "/css/*", "/js/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        UserDetails student1 = User.builder()
                .username("st1")
                .password(passwordEncoder.encode("pass"))
                .roles("student")
                .build();
        return new InMemoryUserDetailsManager(student1);
    }

}
