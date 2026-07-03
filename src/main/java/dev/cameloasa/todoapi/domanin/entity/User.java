package dev.cameloasa.todoapi.domanin.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @Id
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(unique = true)
  private String username;

  private boolean expired;

  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "email"), // PK real
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  public User(String email, String password, String username) {
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public void addRole(Role role) {
    if (role == null) throw new IllegalArgumentException("Role cannot be null");
    roles.add(role);
  }

  public void removeRole(Role role) {
    if (role == null) throw new IllegalArgumentException("Role cannot be null");
    roles.remove(role);
  }
}
