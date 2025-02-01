package bg.uniplovdiv.talkify.friendship.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import bg.uniplovdiv.talkify.friendship.service.FriendshipService;
import bg.uniplovdiv.talkify.security.annotations.Authenticated;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipApi {

  FriendshipService friendshipService;

  @Authenticated
  @PostMapping("/{friendId}")
  @ResponseStatus(CREATED)
  public ResponseEntity<Void> addFriend(@PathVariable @EncodedId Long friendId) {
    friendshipService.addFriend(friendId);
    return ResponseEntity.status(CREATED).build();
  }

  @Authenticated
  @DeleteMapping("/{friendId}")
  @ResponseStatus(NO_CONTENT)
  public ResponseEntity<Void> removeFriend(@PathVariable @EncodedId Long friendId) {
    friendshipService.removeFriend(friendId);
    return ResponseEntity.status(NO_CONTENT).build();
  }
}
