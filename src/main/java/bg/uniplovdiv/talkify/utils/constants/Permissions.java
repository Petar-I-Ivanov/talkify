package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

// have to be manually synchronized with database values
@NoArgsConstructor(access = PRIVATE)
public class Permissions {

  public static final String USER_CREATE = "user:create";
  public static final String USER_SELECT = "user:select";
  public static final String USER_SEARCH = "user:search";
  public static final String USER_UPDATE = "user:update";
  public static final String USER_DELETE = "user:delete";

  public static final String CHANNEL_CREATE = "channel:create";
  public static final String CHANNEL_SELECT = "channel:select";
  public static final String CHANNEL_SEARCH = "channel:search";
  public static final String CHANNEL_UPDATE = "channel:update";
  public static final String CHANNEL_DELETE = "channel:delete";
}
