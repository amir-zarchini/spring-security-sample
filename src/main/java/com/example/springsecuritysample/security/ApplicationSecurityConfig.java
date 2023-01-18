package com.example.springsecuritysample.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.springsecuritysample.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.springsecuritysample.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.example.springsecuritysample.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ApplicationSecurityConfig{

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .requestMatchers("/api/student/**").hasRole(STUDENT.name())
                .requestMatchers(HttpMethod.DELETE,"/api/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .requestMatchers(HttpMethod.POST,"/api/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .requestMatchers(HttpMethod.PUT,"/api/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .requestMatchers(HttpMethod.GET,"/api/management/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
//                .roles(STUDENT.name())
                .authorities(STUDENT.grantedAuthorities())
                .build();
        UserDetails student2 = User.builder()
                .username("st2")
                .password(passwordEncoder.encode("pass2"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.grantedAuthorities())
                .build();
        UserDetails student3 = User.builder()
                .username("st3")
                .password(passwordEncoder.encode("pass3"))
//                .roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.grantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(student1, student2, student3);
    }

}
