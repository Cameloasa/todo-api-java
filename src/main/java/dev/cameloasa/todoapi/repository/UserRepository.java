package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

  // Find if email exists
  Boolean existsByEmail(String email);

  // Find user by email
  Optional<User> findByEmail(String email);

  // Find if email exists and expired status
  boolean existsByEmailAndExpired(String email, boolean expired);

  // Find user by email and expired status
  Optional<User> findByEmailAndExpired(String email, boolean expired);

  // Update expired status by email
  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.expired = :status WHERE u.email = :email")
  void updateExpiredByEmail(@Param("email") String email, @Param("status") boolean status);

  // Update password by email
  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
  void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);
}
