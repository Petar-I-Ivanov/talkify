package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE)
public class EncodedIdSerializer extends StdSerializer<Long> {

  static final long serialVersionUID = 7111698024549345430L;

  @Autowired transient EncodedIdService encryptedIdService;

  public EncodedIdSerializer() {
    super(Long.class);
  }

  @Override
  public void serialize(Long value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    var encodedValue = Optional.ofNullable(value).map(encryptedIdService::encode);
    if (encodedValue.isPresent()) {
      gen.writeString(encodedValue.get());
      return;
    }

    gen.writeNull();
  }
}
