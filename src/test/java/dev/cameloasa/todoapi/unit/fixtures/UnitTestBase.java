package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.converter.AuthConverterImpl;
import dev.cameloasa.todoapi.converter.PersonConverterImpl;
import dev.cameloasa.todoapi.converter.RoleConverterImpl;
import dev.cameloasa.todoapi.converter.TaskConverterImpl;
import dev.cameloasa.todoapi.converter.UserConverterImpl;
import dev.cameloasa.todoapi.repository.PasswordResetTokenRepository;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.SessionRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.AuthServiceImpl;
import dev.cameloasa.todoapi.service.EmailServiceImpl;
import dev.cameloasa.todoapi.service.PersonServiceImpl;
import dev.cameloasa.todoapi.service.SessionService;
import dev.cameloasa.todoapi.service.TaskServiceImpl;
import dev.cameloasa.todoapi.service.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public abstract class UnitTestBase {

  // -------------------------
  // MOCKED REPOSITORIES
  // -------------------------
  @Mock protected UserRepository userRepository;
  @Mock protected PersonRepository personRepository;
  @Mock protected RoleRepository roleRepository;
  @Mock protected SessionRepository sessionRepository;
  @Mock protected TaskRepository taskRepository;
  @Mock protected PasswordResetTokenRepository tokenRepository;

  // -------------------------
  // MOCKED SERVICES
  // -------------------------
  @Mock protected SessionService sessionService;
  @Mock protected PasswordEncoder passwordEncoder;
  @Mock protected EmailServiceImpl emailService;
  

  
  // -------------------------
  // MOCKED Convertors
  // -------------------------
  @Mock protected UserConverterImpl userConverter;
  @Mock protected PersonConverterImpl personConverter;
  @Mock protected RoleConverterImpl roleConverter;
  @Mock protected TaskConverterImpl taskConverter;
  @Mock protected AuthConverterImpl authConverter;


  // -------------------------
  // SYSTEM UNDER TEST
  // -------------------------
  @Mock protected AuthServiceImpl authService;
  @Mock protected TaskServiceImpl taskService;
  @Mock protected PersonServiceImpl personService;
  @Mock protected UserServiceImpl userService;
}
