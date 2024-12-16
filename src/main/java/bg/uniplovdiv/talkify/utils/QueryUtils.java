package bg.uniplovdiv.talkify.utils;

import static lombok.AccessLevel.PRIVATE;

import java.util.Optional;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class QueryUtils {

  public static final String matchStart(String value) {
    return Optional.ofNullable(value).map(val -> val.concat("%")).orElse(null);
  }

  public static final String matchEnd(String value) {
    return Optional.ofNullable(value).map("%"::concat).orElse(null);
  }

  public static final String matchAny(String value) {
    return Optional.ofNullable(value).map("%"::concat).map(val -> val.concat("%")).orElse(null);
  }
}
