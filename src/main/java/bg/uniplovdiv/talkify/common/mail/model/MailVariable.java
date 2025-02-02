package bg.uniplovdiv.talkify.common.mail.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public enum MailVariable {
  RECIEVER("reciever"),
  FRIEND_USERNAME("friend_username"),
  APP_NAME("app_name"),
  USERNAME("username"),
  PASSWORD("password"),
  LOGIN_URL("login_url");

  String variable;
}
