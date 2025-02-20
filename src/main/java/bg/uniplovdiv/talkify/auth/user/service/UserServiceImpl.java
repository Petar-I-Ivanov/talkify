package bg.uniplovdiv.talkify.auth.user.service;

import static bg.uniplovdiv.talkify.auth.user.model.UserPredicates.buildPredicates;
import static bg.uniplovdiv.talkify.common.exception.model.DataValidationException.throwIfCondition;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchPrincipal;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchUserId;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.isPermitted;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.revalidateUser;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static bg.uniplovdiv.talkify.utils.TransactionUtils.afterCommit;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.EMAIL_TAKEN_EXC;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.PASS_MISMATCH_EXC;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.USERNAME_TAKEN_EXC;
import static bg.uniplovdiv.talkify.utils.constants.Permissions.USER_CREATE;
import static bg.uniplovdiv.talkify.utils.constants.Permissions.USER_DELETE;
import static bg.uniplovdiv.talkify.utils.constants.Permissions.USER_UPDATE;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.role.service.RoleService;
import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserRepository;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import bg.uniplovdiv.talkify.common.mail.service.MailService;
import bg.uniplovdiv.talkify.common.model.UniqueValueRequest;
import bg.uniplovdiv.talkify.friendship.service.FriendshipService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  RoleService roleService;
  FriendshipService friendshipService;
  PasswordEncoder passwordEncoder;
  MailService mailService;

  @Override
  public boolean canCreate() {
    return isPermitted(USER_CREATE);
  }

  @Override
  public User create(UserCreateRequest request) {
    throwIfNotAllowed(canCreate());
    var friend = register(request);
    friendshipService.addFriend(friend.getId());

    afterCommit(
        () ->
            mailService.sendMailForRegisteredFriend(fetchPrincipal(), friend, request.password()));
    return friend;
  }

  @Override
  public User register(UserCreateRequest request) {
    throwIfCondition(
        isUsernameExists(new UniqueValueRequest(request.username(), null)), USERNAME_TAKEN_EXC);
    throwIfCondition(isEmailExists(new UniqueValueRequest(request.email(), null)), EMAIL_TAKEN_EXC);
    throwIfCondition(!request.password().matches(request.confirmPassword()), PASS_MISMATCH_EXC);

    var userRole = roleService.getUserRole();
    var encodedPass = passwordEncoder.encode(request.password());
    var user =
        User.builder()
            .username(request.username())
            .email(request.email())
            .password(encodedPass)
            .roles(Set.of(userRole))
            .build();
    user.setActive(true);
    return userRepository.save(user);
  }

  @Override
  public boolean isUsernameExists(UniqueValueRequest request) {
    return Optional.ofNullable(request)
        .map(UniqueValueRequest::exceptId)
        .map(exceptId -> userRepository.existsByUsernameAndIdNot(request.value(), exceptId))
        .orElseGet(() -> userRepository.existsByUsername(request.value()));
  }

  @Override
  public boolean isEmailExists(UniqueValueRequest request) {
    return Optional.ofNullable(request)
        .map(UniqueValueRequest::exceptId)
        .map(exceptId -> userRepository.existsByEmailAndIdNot(request.value(), exceptId))
        .orElseGet(() -> userRepository.existsByEmail(request.value()));
  }

  @Override
  public Optional<User> getByUsernameOrEmail(String usernameOrEmail) {
    return userRepository.findByUsernameOrEmail(usernameOrEmail);
  }

  @Override
  public User getCurrentUser() {
    return getByUsernameOrEmail(fetchPrincipal()).filter(User::isActive).orElse(null);
  }

  @Override
  public User getById(Long id) {
    return userRepository.getReferenceById(id);
  }

  @Override
  public Page<User> getAllByCriteria(UserSearchCriteria criteria, Pageable page) {
    return userRepository.findAll(buildPredicates(criteria), page);
  }

  @Override
  public void addAdminRole(User user) {
    user.getRoles().add(roleService.getChannelAdminRole());
    userRepository.save(user);
  }

  @Override
  public void addGuestRole(User user) {
    user.getRoles().add(roleService.getChannelGuestRole());
    userRepository.save(user);
  }

  @Override
  public boolean canUpdate(User user) {
    return isPermitted(USER_UPDATE) && user.getId().equals(fetchUserId());
  }

  @Override
  public User update(Long id, UserUpdateRequest request) {
    var user = getById(id);
    throwIfNotAllowed(canUpdate(user));

    throwIfCondition(
        isUsernameExists(new UniqueValueRequest(request.username(), id)), USERNAME_TAKEN_EXC);
    throwIfCondition(isEmailExists(new UniqueValueRequest(request.email(), id)), EMAIL_TAKEN_EXC);

    user.setUsername(request.username());
    user.setEmail(request.email());
    revalidateUser(user);
    return user;
  }

  @Override
  public boolean canDelete(User user) {
    return isPermitted(USER_DELETE) && user.getId().equals(fetchUserId());
  }

  @Override
  public void delete(Long id) {
    var user = getById(id);
    throwIfNotAllowed(canDelete(user));
    user.setActive(false);
  }
}
