package dev.cameloasa.todoapi.security;

import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

  private final SessionService sessionService;
  private final UserRepository userRepository;

  public SessionAuthenticationFilter(SessionService sessionService, UserRepository userRepository) {
    this.sessionService = sessionService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println("FILTER EXECUTED ON: " + request.getServletPath());

    // 1. Citește tokenul din header
    String token = request.getHeader("X-Session-Token");

    // 🔥 1.1 Dacă nu e în header, caută în cookie
    if (token == null && request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("session_token")) {
          token = cookie.getValue();
        }
      }
    }

    // 2. Dacă nu există token → continuă fără autentificare
    if (token == null || !sessionService.isValid(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 3. Obține email-ul asociat tokenului
    String email = sessionService.getUserEmail(token);

    if (email == null) {
      filterChain.doFilter(request, response);
      return;
    }

    // 4. Încarcă userul din DB
    User user = userRepository.findByEmailWithRoles(email).orElse(null);

    if (user == null) {
      filterChain.doFilter(request, response);
      return;
    }

    // 5. Creează UserPrincipal (UserDetails)
    UserPrincipal principal = new UserPrincipal(user);

    // 6. Creează Authentication cu rolurile userului
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    // 7. Pune userul în SecurityContext
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 8. Continuă filtrarea
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();

    return path.startsWith("/auth/login")
        || path.startsWith("/auth/register")
        || path.startsWith("/auth/logout")
        || path.startsWith("/auth/me")
        || path.startsWith("/error");
  }
}
