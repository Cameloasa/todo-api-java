package dev.cameloasa.todoapi.config;

import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.security.SessionAuthenticationFilter;
import dev.cameloasa.todoapi.service.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class SecurityConfig {

  private final SessionService sessionService;
  private final UserRepository userRepository;

  public SecurityConfig(SessionService sessionService, UserRepository userRepository) {
    this.sessionService = sessionService;
    this.userRepository = userRepository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth

                    // Public endpoints
                    .requestMatchers("/auth/register", "/auth/login", "/auth/logout", "/auth/me")
                    .permitAll()

                    // SUPERADMIN: create roles
                    .requestMatchers("/auth/roles/create")
                    .hasRole("SUPERADMIN")
                    .requestMatchers("/auth/roles/assign")
                    .hasRole("SUPERADMIN")

                    // ADMIN + SUPERADMIN: view roles
                    .requestMatchers("/auth/roles")
                    .hasAnyRole("ADMIN", "SUPERADMIN")

                    // ADMIN: persons management
                    .requestMatchers("/auth/persons/**")
                    .hasRole("ADMIN")

                    // USER + ADMIN: can view users
                    .requestMatchers("/auth/users/email")
                    .authenticated()
                    .requestMatchers("/auth/users/username")
                    .authenticated()
                    .requestMatchers("/auth/users/all")
                    .authenticated()

                    // ADMIN ONLY: manage users
                    .requestMatchers("/auth/users/disable")
                    .hasRole("ADMIN")
                    .requestMatchers("/auth/users/enable")
                    .hasRole("ADMIN")
                    .requestMatchers("/auth/users")
                    .hasRole("ADMIN") // PATCH + DELETE

                    // USER + ADMIN: can view tasks
                    .requestMatchers("/auth/tasks/my/**")
                    .authenticated()
                    // ADMIN ONLY: manage tasks
                    .requestMatchers("/auth/tasks/**")
                    .hasRole("ADMIN")

                    // Everything else blocked
                    .anyRequest()
                    .denyAll());

    http.addFilterBefore(
        new SessionAuthenticationFilter(sessionService, userRepository),
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
