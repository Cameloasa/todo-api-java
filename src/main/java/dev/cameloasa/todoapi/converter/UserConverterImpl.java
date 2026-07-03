package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.User;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

  private final RoleConverter roleConverter;

  public UserConverterImpl(RoleConverter roleConverter) {
    this.roleConverter = roleConverter;
  }

  @Override
  public UserDTOView toUserDTOView(User entity) {
    if (entity == null) return null;

    return UserDTOView.builder()
        .email(entity.getEmail())
        .expired(entity.isExpired())
        .roles(
            entity.getRoles().stream()
                .map(roleConverter::toRoleDTOView)
                .collect(Collectors.toSet()))
        .build();
  }

  @Override
  public User toUserEntity(UserDTOForm dto) {
    if (dto == null) return null;

    return User.builder()
        .email(dto.getEmail())
        .password(dto.getPassword()) // hashing în service
        .expired(dto.isExpired())
        // rol will be set in service
        .build();
  }
}
