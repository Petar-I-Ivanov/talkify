package bg.uniplovdiv.talkify.friendship.service;

import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchUserId;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import bg.uniplovdiv.talkify.friendship.model.Friendship;
import bg.uniplovdiv.talkify.friendship.model.FriendshipRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipServiceImpl implements FriendshipService {

  FriendshipRepository friendshipRepository;

  UserService userService;
  ChannelService channelService;

  @Override
  public boolean canAddFriend(Long friendId) {
    return !friendshipRepository.existsByUserIdAndFriendIdAndActiveIsTrue(fetchUserId(), friendId);
  }

  @Override
  public List<Friendship> addFriend(Long friendId) {
    throwIfNotAllowed(canAddFriend(friendId));
    var friendships =
        friendshipRepository.findMutualFriendships(fetchUserId(), friendId).stream()
            .map(
                friendship -> {
                  friendship.setActive(true);
                  friendship.getChannel().setActive(true);
                  return friendship;
                })
            .toList();

    if (!friendships.isEmpty()) {
      return friendships;
    }

    var user = userService.getById(fetchUserId());
    var friend = userService.getById(friendId);
    var channel = channelService.createPrivate(user, friend);

    return Stream.of(create(user, friend, channel), create(friend, user, channel))
        .map(friendshipRepository::save)
        .toList();
  }

  @Override
  public Optional<Long> getPrivateChannelId(Long friendId) {
    return friendshipRepository.findChannelIdByUserIdAndFriendIdAndActiveIsTrue(
        fetchUserId(), friendId);
  }

  @Override
  public boolean canRemoveFriend(Long friendId) {
    return friendshipRepository.existsByUserIdAndFriendIdAndActiveIsTrue(fetchUserId(), friendId);
  }

  @Override
  public void removeFriend(Long friendId) {
    throwIfNotAllowed(canRemoveFriend(friendId));
    friendshipRepository
        .findMutualFriendships(fetchUserId(), friendId)
        .forEach(
            friendship -> {
              friendship.setActive(false);
              friendship.getChannel().setActive(false);
            });
  }

  private Friendship create(User user, User friend, Channel channel) {
    var friendship = Friendship.builder().user(user).friend(friend).channel(channel).build();
    friendship.setActive(true);
    return friendship;
  }
}
