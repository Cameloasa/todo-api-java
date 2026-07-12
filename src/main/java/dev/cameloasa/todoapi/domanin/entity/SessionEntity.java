package dev.cameloasa.todoapi.domanin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SessionEntity {

  @Id private String token;

  @Column(nullable = false)
  private String userEmail;

  @Column(nullable = false)
  private long createdAt;

  @Column(nullable = false)
  private long expiresAt;
}
