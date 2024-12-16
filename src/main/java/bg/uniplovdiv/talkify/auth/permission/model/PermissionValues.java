package bg.uniplovdiv.talkify.auth.permission.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public enum PermissionValues {
  USER_SEARCH("user:search"),
  USER_CREATE("user:create"),
  CHANNEL_CREATE("channel:create"),
  CHANNEL_SEARCH("channel:search");

  String value;
}
