package com.example.springsecuritysample.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.springsecuritysample.security.ApplicationUserPermission.*;
import static com.example.springsecuritysample.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig{

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/**", "index", "/css/*", "/js/*", "api/**").permitAll()
                .requestMatchers("api/student/*").hasRole(STUDENT.name())
                .requestMatchers(HttpMethod.DELETE,"api/management/*").hasAnyAuthority(COURSE_WRITE.name())
                .requestMatchers(HttpMethod.POST,"api/management/*").hasAnyAuthority(COURSE_WRITE.name())
                .requestMatchers(HttpMethod.PUT,"api/management/*").hasAnyAuthority(COURSE_WRITE.name())
                .requestMatchers("api/management/*").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
                .roles(STUDENT.name())
                .build();
        UserDetails student2 = User.builder()
                .username("st2")
                .password(passwordEncoder.encode("pass2"))
                .roles(ADMIN.name())
                .build();
        UserDetails student3 = User.builder()
                .username("st3")
                .password(passwordEncoder.encode("pass3"))
                .roles(ADMINTRAINEE.name())
                .build();
        return new InMemoryUserDetailsManager(student1, student2, student3);
    }

}
