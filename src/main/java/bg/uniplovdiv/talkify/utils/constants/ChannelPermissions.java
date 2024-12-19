package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.channel.model.Channel;
import java.util.Optional;
import lombok.NoArgsConstructor;

// ugly af, do something...
@NoArgsConstructor(access = PRIVATE)
public class ChannelPermissions {

  public static final String ALL = "*";
  public static final String SEND_MESSAGE = "sendMessage";
  public static final String CHANGE_NAME = "changeName";
  public static final String ADD_GUEST = "addGuest";
  public static final String MAKE_ADMIN = "makeAdmin";
  public static final String REMOVE_GUEST = "removeGuest";
  public static final String DELETE_CHANNEL = "deleteChannel";

  public static final String buildOwnedChannelPermissions(Channel channel) {
    var ownerPermissions = getJoinedOwnerPermissions();
    return buildChannelPermissions(channel, ownerPermissions);
  }

  public static final String buildAdminChannelPermissions(Channel channel) {
    var adminPermissions = getJoinedAdminPermissions();
    return buildChannelPermissions(channel, adminPermissions);
  }

  public static final String buildGuestChannelPermissions(Channel channel) {
    var guestPermissions = getJoinedGuestPermissions();
    return buildChannelPermissions(channel, guestPermissions);
  }

  private static String getJoinedGuestPermissions() {
    return SEND_MESSAGE;
  }

  private static String getJoinedAdminPermissions() {
    return String.join(",", getJoinedGuestPermissions(), CHANGE_NAME, ADD_GUEST);
  }

  private static String getJoinedOwnerPermissions() {
    return ALL;
  }

  private static String buildChannelPermissions(Channel channel, String joinedPermissions) {
    return Optional.ofNullable(channel)
        .map(Channel::getId)
        .map(String::valueOf)
        .map(id -> id + ":" + joinedPermissions)
        .orElse(null);
  }
}
