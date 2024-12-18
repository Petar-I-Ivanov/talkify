package bg.uniplovdiv.talkify.auth.user.service;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  User createUser(UserCreateRequest request);

  User getCurrentUser();

  User getById(Long id);

  Optional<User> getByUsernameOrEmail(String usernameOrEmail);

  Page<User> getUsersByCriteria(UserSearchCriteria criteria, Pageable page);

  User updateUser(Long id, UserUpdateRequest request);

  void deleteUser(Long id);
}
