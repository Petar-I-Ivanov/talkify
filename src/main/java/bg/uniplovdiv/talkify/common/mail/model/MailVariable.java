package bg.uniplovdiv.talkify.common.mail.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public enum MailVariable {
  INIT("");

  String variable;
}
