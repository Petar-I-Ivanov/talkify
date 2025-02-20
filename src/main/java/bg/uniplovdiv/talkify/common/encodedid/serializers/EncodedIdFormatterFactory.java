package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static bg.uniplovdiv.talkify.utils.EncodedIdUtil.decoded;
import static bg.uniplovdiv.talkify.utils.EncodedIdUtil.encode;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;
import lombok.experimental.FieldDefaults;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

@FieldDefaults(level = PRIVATE)
public class EncodedIdFormatterFactory implements AnnotationFormatterFactory<EncodedId> {

  static final Formatter<Long> FORMATTER;

  static {
    FORMATTER =
        new Formatter<Long>() {

          @Override
          public String print(Long internalId, Locale locale) {
            return encode(internalId);
          }

          @Override
          public Long parse(String publicId, Locale locale) throws ParseException {
            return decoded(publicId);
          }
        };
  }

  public Set<Class<?>> getFieldTypes() {
    return Set.of(Long.class);
  }

  @Override
  public Printer<Long> getPrinter(EncodedId annotation, Class<?> fieldType) {
    return FORMATTER;
  }

  @Override
  public Parser<Long> getParser(EncodedId annotation, Class<?> fieldType) {
    return FORMATTER;
  }
}
