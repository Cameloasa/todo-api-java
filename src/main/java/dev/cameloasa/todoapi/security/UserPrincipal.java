package dev.cameloasa.todoapi.security;

import dev.cameloasa.todoapi.domanin.entity.User;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

  private final User user;

  public UserPrincipal(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail(); // email = username în sistemul tău
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.isExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // nu ai lock logică
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // nu ai expirare credențiale
  }

  @Override
  public boolean isEnabled() {
    return !user.isExpired();
  }

  public User getUser() {
    return user;
  }
}
