package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EncodedIdDeserializer extends StdDeserializer<Long> {

  static final long serialVersionUID = 3688917752055750600L;

  transient EncodedIdService encryptedIdService;

  public EncodedIdDeserializer(EncodedIdService encryptedIdService) {
    super(Long.class);
    this.encryptedIdService = encryptedIdService;
  }

  @Override
  public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Optional.ofNullable(p.getText()).map(encryptedIdService::decode).orElse(null);
  }
}
