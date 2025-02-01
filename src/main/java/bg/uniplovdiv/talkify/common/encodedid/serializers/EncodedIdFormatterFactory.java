package bg.uniplovdiv.talkify.common.encodedid.serializers;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EncodedIdFormatterFactory implements AnnotationFormatterFactory<EncodedId> {

  EncodedIdService encodedIdService;

  public Set<Class<?>> getFieldTypes() {
    return Set.of(Long.class);
  }

  @Override
  public Printer<Long> getPrinter(EncodedId annotation, Class<?> fieldType) {
    return buildFormatter(encodedIdService);
  }

  @Override
  public Parser<Long> getParser(EncodedId annotation, Class<?> fieldType) {
    return buildFormatter(encodedIdService);
  }

  private static Formatter<Long> buildFormatter(EncodedIdService encodedIdService) {
    return new Formatter<Long>() {

      @Override
      public String print(Long internalId, Locale locale) {
        return encodedIdService.encode(internalId);
      }

      @Override
      public Long parse(String publicId, Locale locale) throws ParseException {
        return encodedIdService.decode(publicId);
      }
    };
  }
}
