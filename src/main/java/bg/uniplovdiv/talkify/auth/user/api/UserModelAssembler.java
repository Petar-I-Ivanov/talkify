package bg.uniplovdiv.talkify.auth.user.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.common.PagedRepresentationAssembler;
import bg.uniplovdiv.talkify.friendship.api.FriendshipApi;
import bg.uniplovdiv.talkify.friendship.service.FriendshipService;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserModelAssembler extends PagedRepresentationAssembler<User, UserModel> {

  UserService userService;
  FriendshipService friendshipService;

  public UserModelAssembler(UserService userService, FriendshipService friendshipService) {
    super(UserApi.class, UserModel.class);
    this.userService = userService;
    this.friendshipService = friendshipService;
  }

  @Override
  public UserModel toModel(User user) {
    return UserModel.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .privateChannelId(friendshipService.getPrivateChannelId(user.getId()).orElse(null))
        .build()
        .addIf(
            userService.canUpdate(user),
            () -> linkTo(methodOn(UserApi.class).update(user.getId(), null)).withRel("update"))
        .addIf(
            userService.canDelete(user),
            () -> linkTo(methodOn(UserApi.class).delete(user.getId())).withRel("delete"))
        .addIf(
            friendshipService.canAddFriend(user.getId()),
            () ->
                linkTo(methodOn(FriendshipApi.class).addFriend(user.getId())).withRel("addFriend"))
        .addIf(
            friendshipService.canRemoveFriend(user.getId()),
            () ->
                linkTo(methodOn(FriendshipApi.class).removeFriend(user.getId()))
                    .withRel("removeFriend"));
  }
}
