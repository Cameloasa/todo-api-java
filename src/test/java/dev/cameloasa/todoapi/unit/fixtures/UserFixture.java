package dev.cameloasa.todoapi.unit.fixtures;

import java.util.List;
import java.util.Set;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;

public class UserFixture {

  public static User sampleUser() {
        User user = new User();
        user.setEmail("test@example.com"); // user Id
        user.setUsername("test");
        user.setPassword("Password123!"); // raw password
        user.setExpired(false);

        // roles
        Role role = RoleFixture.sampleRole();
        user.setRoles(Set.of(role));

        return user;
    }

    public static UserDTOForm sampleUserDTOForm() {
        UserDTOForm dto = new UserDTOForm();
        dto.setEmail("test@example.com");
        dto.setUsername("test");
        dto.setPassword("Password123!");
        dto.setExpired(false);
        dto.setRoleIds(List.of(1L)); 
        return dto;
    }

    public static UserDTOView sampleUserDTOView() {
        return UserDTOView.builder()
                .email("test@example.com")
                .username("test")
                .expired(false)
                .roles(Set.of(RoleFixture.sampleRoleDTOView()))
                .build();
    }

    public static User sampleUserWithEncodedPassword() {
        User user = sampleUser();
        user.setPassword("encoded-password");
        return user;
    }

    public static User sampleUserWithCustomEmail(String email) {
        User user = sampleUser();
        user.setEmail(email);
        return user;
    }
}
