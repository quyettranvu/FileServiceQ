package com.file.servicer.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/upload").authenticated()
                        .requestMatchers("/stats").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(); // or JWT / formLogin for production
        return http.build();
    }

    // Mock user in memory
    @Bean
    public InMemoryUserDetailsManager userDetailsSevice() {
        @SuppressWarnings("deprecation")
        UserDetails user = User.withDefaultPasswordEncoder()
                            .username("user")
                            .password("1234")
                            .roles("USER")
                            .build();

        return new InMemoryUserDetailsManager(user);
    }
}
