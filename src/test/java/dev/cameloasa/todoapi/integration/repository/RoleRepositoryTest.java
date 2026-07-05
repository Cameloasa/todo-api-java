package dev.cameloasa.todoapi.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleRepositoryTest {

  @Autowired private RoleRepository roleRepository;

  private Role roleUser;
  private Role roleAdmin;

  @BeforeEach
  void setUp() {
    roleUser = new Role();
    roleUser.setName("USER");
    roleRepository.save(roleUser);

    roleAdmin = new Role();
    roleAdmin.setName("ADMIN");
    roleRepository.save(roleAdmin);
  }

  // ---------------------------------------------------------
  // TEST 1: findByName
  // ---------------------------------------------------------
  @Test
  void testFindByName() {
    Optional<Role> found = roleRepository.findByName("USER");

    assertTrue(found.isPresent());
    assertEquals("USER", found.get().getName());
  }

  // ---------------------------------------------------------
  // TEST 2: existsByName
  // ---------------------------------------------------------
  @Test
  void testExistsByName() {
    assertTrue(roleRepository.existsByName("ADMIN"));
    assertFalse(roleRepository.existsByName("MANAGER"));
  }

  // ---------------------------------------------------------
  // TEST 3: findAllByNameIn
  // ---------------------------------------------------------
  @Test
  void testFindAllByNameIn() {
    List<Role> roles = roleRepository.findAllByNameIn(List.of("USER", "ADMIN"));

    assertEquals(2, roles.size());
  }
}
