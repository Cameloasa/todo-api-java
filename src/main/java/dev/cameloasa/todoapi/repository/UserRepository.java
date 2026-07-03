package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :value OR u.username = :value")
    Optional<User> findByEmailOrUsername(@Param("value") String value);

    boolean existsByEmailAndExpired(String email, boolean expired);
    
    Optional<User> findByEmailAndExpired(String email, boolean expired);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.expired = :status WHERE u.email = :email")
    void updateExpiredByEmail(@Param("email") String email, @Param("status") boolean status);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);
}

