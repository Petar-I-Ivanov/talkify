package bg.uniplovdiv.talkify.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class Regex {

  // at least 1 upper case, 1 lower case and (1 digit or 1 symbol)
  public static final String PASSWORD_PTRN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).+$";

  public static final String VAL_AFTER_LAST_SLASH_PTRN = ".*/([^/]+)$";
}
