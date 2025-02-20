package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static bg.uniplovdiv.talkify.utils.EncodedIdUtil.encode;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public class EncodedIdSerializer extends StdSerializer<Long> {

  static final long serialVersionUID = 7111698024549345430L;

  public EncodedIdSerializer() {
    super(Long.class);
  }

  @Override
  public void serialize(Long value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    var encodedValue = encode(value);
    if (encodedValue != null) {
      gen.writeString(encodedValue);
      return;
    }

    gen.writeNull();
  }
}
