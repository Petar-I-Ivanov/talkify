package bg.uniplovdiv.talkify.friendship.service;

import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchUserId;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.model.UserRepository;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Lazy)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipServiceImpl implements FriendshipService {

  FriendshipRepository friendshipRepository;
  UserRepository userRepository;

  ChannelService channelService;

  @Override
  public boolean canAddFriend(Long friendId) {
    var currentUserId = fetchUserId();
    return !currentUserId.equals(friendId)
        && !friendshipRepository.existsByUserIdAndFriendIdAndActiveIsTrue(currentUserId, friendId);
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

    var user = userRepository.getReferenceById(fetchUserId());
    var friend = userRepository.getReferenceById(friendId);
    var channel = channelService.createPrivate(user, friend);

    return Stream.of(create(user, friend, channel), create(friend, user, channel))
        .map(friendshipRepository::save)
        .toList();
  }

  @Override
  public Optional<Long> getPrivateChannelId(Long friendId) {
    return friendshipRepository
        .findByUserIdAndFriendIdAndActiveIsTrue(fetchUserId(), friendId)
        .map(Friendship::getChannel)
        .map(Channel::getId);
  }

  @Override
  public boolean canRemoveFriend(Long friendId) {
    var currentUserId = fetchUserId();
    return !currentUserId.equals(friendId)
        && friendshipRepository.existsByUserIdAndFriendIdAndActiveIsTrue(currentUserId, friendId);
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
