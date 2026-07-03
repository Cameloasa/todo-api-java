package dev.cameloasa.todoapi.startup;

import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {

  private final RoleRepository roleRepository;

  public RoleDataLoader(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    roleRepository.save(new Role("ADMIN"));
    roleRepository.save(new Role("USER"));
    roleRepository.save(new Role("GUEST"));
  }
}
