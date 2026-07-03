package dev.cameloasa.todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cameloasa.todoapi.converter.RoleConverter;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }



    @Override
    public List<RoleDTOView> getAll() {
        List <Role> roles =roleRepository.findAll();
        List<RoleDTOView> roleDTOViewsList = new ArrayList<>();

        for (Role entity : roles) {

            roleDTOViewsList.add(roleConverter.toRoleDTO(entity));
        }
        return roleDTOViewsList;
    }
}
