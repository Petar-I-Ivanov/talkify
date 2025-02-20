package bg.uniplovdiv.talkify.auth.user.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import bg.uniplovdiv.talkify.auth.user.model.UserCreateRequest;
import bg.uniplovdiv.talkify.auth.user.model.UserSearchCriteria;
import bg.uniplovdiv.talkify.auth.user.model.UserUpdateRequest;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import bg.uniplovdiv.talkify.common.model.UniqueValueRequest;
import bg.uniplovdiv.talkify.security.annotations.Authenticated;
import bg.uniplovdiv.talkify.security.annotations.PublicAccess;
import bg.uniplovdiv.talkify.security.annotations.permissions.user.UserCreate;
import bg.uniplovdiv.talkify.security.annotations.permissions.user.UserDelete;
import bg.uniplovdiv.talkify.security.annotations.permissions.user.UserSearch;
import bg.uniplovdiv.talkify.security.annotations.permissions.user.UserSelect;
import bg.uniplovdiv.talkify.security.annotations.permissions.user.UserUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserApi {

  UserService userService;
  UserModelAssembler userModelAssembler;

  @UserCreate
  @PostMapping
  @ResponseStatus(CREATED)
  public UserModel create(@Valid @RequestBody UserCreateRequest request) {
    return userModelAssembler.toModel(userService.create(request));
  }

  @PublicAccess
  @PostMapping("/register")
  @ResponseStatus(CREATED)
  public UserModel register(@Valid @RequestBody UserCreateRequest request) {
    return userModelAssembler.toModel(userService.register(request));
  }

  @PublicAccess
  @GetMapping("/exists/username")
  @ResponseStatus(OK)
  public boolean existsByName(@Valid UniqueValueRequest request) {
    return userService.isUsernameExists(request);
  }

  @PublicAccess
  @GetMapping("/exists/email")
  @ResponseStatus(OK)
  public boolean existsByEmail(@Valid UniqueValueRequest request) {
    return userService.isEmailExists(request);
  }

  @UserSelect
  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public UserModel getById(@PathVariable @EncodedId Long id) {
    return userModelAssembler.toModel(userService.getById(id));
  }

  @Authenticated
  @GetMapping("/current")
  @ResponseStatus(OK)
  public UserModel getCurrent() {
    return userModelAssembler.toModel(userService.getCurrentUser());
  }

  @UserSearch
  @GetMapping
  @ResponseStatus(OK)
  public PagedModel<UserModel> getAllByCriteria(UserSearchCriteria criteria, Pageable page) {
    return userModelAssembler.toPagedModel(userService.getAllByCriteria(criteria, page));
  }

  @UserUpdate
  @PutMapping("/{id}")
  @ResponseStatus(OK)
  public UserModel update(
      @PathVariable @EncodedId Long id, @Valid @RequestBody UserUpdateRequest request) {
    return userModelAssembler.toModel(userService.update(id, request));
  }

  @UserDelete
  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable @EncodedId Long id) {
    userService.delete(id);
    return ResponseEntity.status(NO_CONTENT.value()).build();
  }
}
