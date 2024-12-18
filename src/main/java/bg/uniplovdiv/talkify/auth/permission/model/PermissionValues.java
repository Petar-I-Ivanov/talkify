package bg.uniplovdiv.talkify.auth.permission.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class PermissionValues {

  public static final String USER_CREATE = "user:create";
  public static final String USER_GET = "user:get";
  public static final String USER_SEARCH = "user:search";
  public static final String USER_UPDATE = "user:update";
  public static final String USER_DELETE = "user:delete";

  public static final String CHANNEL_CREATE = "channel:create";
  public static final String CHANNEL_GET = "channel:get";
  public static final String CHANNEL_SEARCH = "channel:search";
  public static final String CHANNEL_UPDATE = "channel:update";
  public static final String CHANNEL_DELETE = "channel:delete";
}
