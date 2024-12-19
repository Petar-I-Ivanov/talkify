package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

// have to be manually synchronized with database names
@NoArgsConstructor(access = PRIVATE)
public class Roles {

  public static final String USER = "USER";
  public static final String CHANNEL_OWNER = "CHANNEL_OWNER";
  public static final String CHANNEL_ADMIN = "CHANNEL_ADMIN";
  public static final String CHANNEL_GUEST = "CHANNEL_GUEST";
}
