package dev.cameloasa.todoapi.service;

import org.springframework.stereotype.Service;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;

import java.util.List;


public interface RoleService {
    List<RoleDTOView> getAll();
}
