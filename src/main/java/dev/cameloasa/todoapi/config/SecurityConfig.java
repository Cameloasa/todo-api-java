package dev.cameloasa.todoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers("/auth/register", "/auth/login", "/auth/logout", "/auth/me")
                .permitAll()

                // SUPERADMIN: create roles
                .requestMatchers("/auth/roles/create").hasRole("SUPERADMIN")

                // ADMIN + SUPERADMIN: view roles
                .requestMatchers("/auth/roles").hasAnyRole("ADMIN", "SUPERADMIN")

                // ADMIN: persons management
                .requestMatchers("/auth/persons/**").hasRole("ADMIN")

                // ADMIN: users management
                .requestMatchers("/auth/users/**").hasRole("ADMIN")

                // Everything else blocked
                .anyRequest().denyAll()
            );


              

        return http.build();
    }
}
