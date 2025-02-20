package bg.uniplovdiv.talkify.auth.user.service;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import bg.uniplovdiv.talkify.common.model.UniqueValueRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  boolean canCreate();

  User create(UserCreateRequest request);

  User register(UserCreateRequest request);

  boolean isUsernameExists(UniqueValueRequest request);

  boolean isEmailExists(UniqueValueRequest request);

  User getCurrentUser();

  User getById(Long id);

  Optional<User> getByUsernameOrEmail(String usernameOrEmail);

  Page<User> getAllByCriteria(UserSearchCriteria criteria, Pageable page);

  void addAdminRole(User user);

  void addGuestRole(User user);

  boolean canUpdate(User user);

  User update(Long id, UserUpdateRequest request);

  boolean canDelete(User user);

  void delete(Long id);
}
