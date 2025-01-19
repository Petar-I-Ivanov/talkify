package bg.uniplovdiv.talkify.friendship.service;

import bg.uniplovdiv.talkify.friendship.model.Friendship;
import java.util.List;
import java.util.Optional;

public interface FriendshipService {

  boolean canAddFriend(Long friendId);

  List<Friendship> addFriend(Long friendId);

  Optional<Long> getPrivateChannelId(Long friendId);

  boolean canRemoveFriend(Long friendId);

  void removeFriend(Long friendId);
}
