package dev.cameloasa.todoapi.domanin.dto;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder


public class UserDTOView {
    private String email;
    private Set<RoleDTOView> roles;
}
