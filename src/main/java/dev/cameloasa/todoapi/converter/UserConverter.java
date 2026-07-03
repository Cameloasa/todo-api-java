package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.User;

public interface UserConverter {

  UserDTOView toUserDTOView(User entity);

  User toUserEntity(UserDTOForm dto);
}
