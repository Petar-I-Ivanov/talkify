package bg.uniplovdiv.talkify.auth.user.service;

import bg.uniplovdiv.talkify.auth.user.model.UniqueEmailRequest;
import bg.uniplovdiv.talkify.auth.user.model.UniqueUsernameRequest;
import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  boolean canCreate();

  User create(UserCreateRequest request);

  boolean isUsernameExists(UniqueUsernameRequest request);

  boolean isEmailExists(UniqueEmailRequest request);

  User getCurrentUser();

  User getById(Long id);

  Optional<User> getByUsernameOrEmail(String usernameOrEmail);

  Page<User> getAllByCriteria(UserSearchCriteria criteria, Pageable page);

  boolean canUpdate(User user);

  User update(Long id, UserUpdateRequest request);

  boolean canDelete(User user);

  void delete(Long id);
}
