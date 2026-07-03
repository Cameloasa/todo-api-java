package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RoleRepositoryTest {
  @Autowired private RoleRepository roleRepository;

  private Role role;

  @BeforeEach
  public void setUp() {
    role = new Role();
    role.setName("USER");
    roleRepository.save(role);
  }
  /*@Test
  void testFindByName() {
      Optional<Role> foundRole = roleRepository.findByName("USER");
      assertTrue(foundRole.isPresent());
      assertEquals("USER", foundRole.get().getName());

  }*/
}
