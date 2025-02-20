package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static bg.uniplovdiv.talkify.utils.EncodedIdUtil.decoded;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public class EncodedIdDeserializer extends StdDeserializer<Long> {

  static final long serialVersionUID = 3688917752055750600L;

  public EncodedIdDeserializer() {
    super(Long.class);
  }

  @Override
  public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return decoded(p.getText());
  }
}
