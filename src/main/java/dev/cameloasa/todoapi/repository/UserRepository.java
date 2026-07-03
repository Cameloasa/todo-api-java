package dev.cameloasa.todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cameloasa.todoapi.domanin.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //Check if the email exists in the database
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    //Update expired by email
    @Modifying
    @Query("UPDATE User u SET u.expired = : status WHERE u.email = : email")
    void updateExpiredByEmail(@Param("email") String email , @Param("status") boolean status);

    //Update password by email
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);
}
