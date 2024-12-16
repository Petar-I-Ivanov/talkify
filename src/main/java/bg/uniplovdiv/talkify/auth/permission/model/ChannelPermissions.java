package bg.uniplovdiv.talkify.auth.permission.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChannelPermissions {

  public static final String SEND_MESSAGE = "sendMessage";
  public static final String CHANGE_NAME = "changeName";
  public static final String ADD_GUEST = "addGuest";
  public static final String MAKE_ADMIN = "makeAdmin";
  public static final String REMOVE_GUEST = "removeGuest";
  public static final String DELETE_CHANNEL = "deleteChannel";
}
