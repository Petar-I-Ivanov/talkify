package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.channel.model.Channel;
import java.util.Optional;
import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChannelPermissions {

  private static final String ALL = "*";

  public static final String SEND_MESSAGE = "sendMessage";
  public static final String CHANGE_NAME = "changeName";
  public static final String ADD_GUEST = "addGuest";
  public static final String MAKE_ADMIN = "makeAdmin";
  public static final String REMOVE_GUEST = "removeGuest";
  public static final String DELETE_CHANNEL = "deleteChannel";

  public static final String buildOwnedChannelPermissions(Channel channel) {
    var ownerPermissions = getOwnerPermissions();
    return buildChannelPermissions(channel, ownerPermissions);
  }

  public static final String buildAdminChannelPermissions(Channel channel) {
    var adminPermissions = getAdminPermissions();
    return buildChannelPermissions(channel, adminPermissions);
  }

  public static final String buildGuestChannelPermissions(Channel channel) {
    var guestPermissions = getGuestPermissions();
    return buildChannelPermissions(channel, guestPermissions);
  }

  private static Set<String> getGuestPermissions() {
    return Set.of(SEND_MESSAGE);
  }

  private static Set<String> getAdminPermissions() {
    return Set.of(SEND_MESSAGE, CHANGE_NAME, ADD_GUEST);
  }

  private static Set<String> getOwnerPermissions() {
    return Set.of(ALL);
  }

  private static String buildChannelPermissions(Channel channel, Set<String> permissions) {
    return Optional.ofNullable(channel)
        .map(Channel::getId)
        .map(String::valueOf)
        .map(id -> id + ":" + joinPermissions(permissions))
        .orElse(null);
  }

  private static String joinPermissions(Set<String> permission) {
    return Optional.ofNullable(permission).map(p -> String.join(",", p)).orElse("");
  }
}
