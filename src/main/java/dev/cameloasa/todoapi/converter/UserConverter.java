package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.User;

public interface UserConverter {

    UserDTOView toUserDTO(User entity);
    User toUserEntity(UserDTOView dto);
}
