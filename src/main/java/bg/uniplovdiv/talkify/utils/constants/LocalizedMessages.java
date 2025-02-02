package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = PRIVATE)
public enum LocalizedMessages {
  NOT_FOUND_EXC("errors.notFound"),
  USERNAME_TAKEN_EXC("errors.usernameTaken"),
  EMAIL_TAKEN_EXC("errors.emailTaken"),
  PASS_MISMATCH_EXC("errors.passMismatch"),
  NAME_TAKEN_EXC("errors.nameTaken"),
  USER_ALREADY_IN_CHANNEL_EXC("errors.userAlreadyInChannel"),
  NOT_PERMITTED_EXC("errors.notPermitted"),
  CHANNEL_NOT_PERMITTED_EXC("errors.channelNotPermitted"),
  INVALID_CHANNEL_EXC("errors.invalidChannel");

  String key;
}
