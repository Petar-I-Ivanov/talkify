package bg.uniplovdiv.talkify.auth.user.service;

import static bg.uniplovdiv.talkify.auth.role.model.RoleName.USER;
import static bg.uniplovdiv.talkify.auth.user.model.UserPredicates.buildPredicates;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchPrincipal;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.role.service.RoleService;
import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserRepository;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// add can create/edit/delete methods and validate
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  RoleService roleService;
  PasswordEncoder passwordEncoder;

  // add email/username unique validations and password/confirmPasswords
  @Override
  public User createUser(UserCreateRequest request) {
    var userRole = roleService.getRoleByName(USER);
    var encodedPass = passwordEncoder.encode(request.password());
    var user =
        User.builder()
            .username(request.username())
            .email(request.email())
            .password(encodedPass)
            .roles(Set.of(userRole))
            .build();
    return userRepository.save(user);
  }

  @Override
  public Optional<User> getByUsernameOrEmail(String usernameOrEmail) {
    return userRepository.findByUsernameOrEmailAndActiveTrue(usernameOrEmail);
  }

  // remove hard-coded string with username from SpringSecurity context
  @Override
  public User getCurrentUser() {
    return getByUsernameOrEmail(fetchPrincipal()).orElse(null);
  }

  @Override
  public User getById(Long id) {
    return userRepository.getReferenceById(id);
  }

  @Override
  public Page<User> getUsersByCriteria(UserSearchCriteria criteria, Pageable page) {
    return userRepository.findAll(buildPredicates(criteria), page);
  }

  // add email/username unique validations
  @Override
  public User updateUser(Long id, UserUpdateRequest request) {
    var user = getById(id);
    user = user.toBuilder().username(request.username()).email(request.email()).build();
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(Long id) {
    var user = getById(id);
    user.setActive(false);
  }
}
