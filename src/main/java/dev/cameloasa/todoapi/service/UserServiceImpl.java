package dev.cameloasa.todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public UserDTOView register(UserDTOForm dtoForm) {
        //1.Check the param
        if (dtoForm == null) throw new IllegalArgumentException("UserDTOForm cannot be null");
        //2. Check if the email exist in the database
        boolean emailExists = userRepository.existsByEmail(dtoForm.getEmail());
        //if email exist throw an Exception - Data Duplicate Exception
        if (emailExists) {
            throw new DataDuplicateException("Email already exists");
        }
        //3. Validate roles in the repository using Role Set and stream for dtoForm
        Set<Role> roleList = dtoForm.getRoles()
                .stream()
                .map(
                        roleDTOForm -> roleRepository.findById(roleDTOForm.getId())
                                .orElseThrow(()-> new DataNotFoundException("Role is not valid")))
                .collect(Collectors.toSet());
        //4 & 5. Convert UserDTOForm to User entity using Builder and hash the password
        User user = User.builder()
                        .email(dtoForm.getEmail())
                                .password(passwordEncoder.encode(dtoForm.getPassword()))
                                        .roles(roleList)
                                                .build();
        //6. Save the User to the database
        User savedUser = userRepository.save(user);

        //7. Convert the repository result to UserDTOView for RoleDTOView
        Set<RoleDTOView> roleDTOViews = savedUser.getRoles()
                .stream()
                .map(role -> RoleDTOView.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toSet());

        //8. return the result using Builder
        return UserDTOView.builder()
                .email(savedUser.getEmail())
                .roles(roleDTOViews)
                .build();
    }

    @Override
    public UserDTOView getByEmail(String email) {
        //1. check if the user's email is in DB(repository) using find by Id
        User user = userRepository.findById(email)
                .orElseThrow(()-> new DataNotFoundException("Email not found."));
        //2. Convert the repository result to UserDTOView for RoleDTOView
        Set<RoleDTOView> roleDTOViews = user.getRoles()
                .stream()
                .map(role -> RoleDTOView.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toSet());

        //3. return the result using Builder
        return UserDTOView.builder()
                .email(user.getEmail())
                .roles(roleDTOViews)
                .build();
    }

    @Override
    @Transactional
    public void disableEmail(String email) {
        isEmailTaken(email);
        userRepository.updateExpiredByEmail(email,true);

    }

    @Override
    @Transactional
    public void enableEmail(String email) {
        isEmailTaken(email);
        userRepository.updateExpiredByEmail(email,false);

    }

    private void isEmailTaken(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new DataNotFoundException("Email not found.");
        }
    }

}
