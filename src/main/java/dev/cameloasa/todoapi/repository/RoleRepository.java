package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  // Find role by name
  Optional<Role> findByName(String name);

  // Check if role name exists
  boolean existsByName(String name);

  // Find all roles by a list of names
  List<Role> findAllByNameIn(List<String> names);
}
