package se.lexicon.g49todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
  @Autowired private UserRepository userRepository;
  private User user;

  @BeforeEach
  void setUp() {
    User user = User.builder().email("test@gmail.com").password("password").build();

    userRepository.save(user);
  }
}
