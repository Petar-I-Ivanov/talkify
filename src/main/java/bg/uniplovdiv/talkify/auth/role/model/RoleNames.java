package bg.uniplovdiv.talkify.auth.role.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class RoleNames {

  public static final String USER = "USER";
  public static final String CHANNEL_OWNER = "CHANNEL_OWNER";
  public static final String CHANNEL_ADMIN = "CHANNEL_ADMIN";
  public static final String CHANNEL_GUEST = "CHANNEL_GUEST";
}
