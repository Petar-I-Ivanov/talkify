package bg.uniplovdiv.talkify.auth.user.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.common.PagedRepresentationAssembler;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserModelAssembler extends PagedRepresentationAssembler<User, UserModel> {

  UserService userService;

  public UserModelAssembler(UserService userService) {
    super(UserApi.class, UserModel.class);
    this.userService = userService;
  }

  @Override
  public UserModel toModel(User user) {
    return UserModel.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .build()
        .addIf(
            userService.canUpdate(user),
            () -> linkTo(methodOn(UserApi.class).update(user.getId(), null)).withRel("update"))
        .addIf(
            userService.canDelete(user),
            () -> linkTo(methodOn(UserApi.class).delete(user.getId())).withRel("delete"));
  }
}
