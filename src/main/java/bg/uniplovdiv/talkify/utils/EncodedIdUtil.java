package bg.uniplovdiv.talkify.utils;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class EncodedIdUtil {

  static final EncodedIdService ENCODER;

  static {
    var key = System.getProperty("application.encoded-id-key", "FA1234BC5678DE90A1B2C3D4E5F60789");
    ENCODER = EncodedIdService.getInstance(key);
  }

  public static final String encode(Long decodedId) {
    return ENCODER.encode(decodedId);
  }

  public static final Long decoded(String encodedId) {
    return ENCODER.decode(encodedId);
  }
}
