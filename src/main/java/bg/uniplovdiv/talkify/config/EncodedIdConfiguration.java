package bg.uniplovdiv.talkify.config;

import static lombok.AccessLevel.PRIVATE;

import at.favre.lib.idmask.IdMask;
import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import bg.uniplovdiv.talkify.common.encodedid.serializers.EncodedIdFormatterFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EncodedIdConfiguration implements WebMvcConfigurer {

  ApplicationProperties properties;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatterForFieldAnnotation(new EncodedIdFormatterFactory(encodedIdService()));
  }

  @Bean
  public EncodedIdService encodedIdService() {
    return Optional.ofNullable(properties.getEncodedIdKey())
        .map(EncodedIdService::buildMask)
        .map(EncodedIdConfiguration::buildMaskingEncodedIdService)
        .orElseGet(EncodedIdConfiguration::buildNoopEncodedIdService);
  }

  private static EncodedIdService buildMaskingEncodedIdService(IdMask<Long> masker) {
    return new EncodedIdService() {

      @Override
      public String encode(Long internalId) {
        return Optional.ofNullable(internalId).map(masker::mask).orElse(null);
      }

      public Long decode(String publicId) {
        return Optional.ofNullable(publicId).map(masker::unmask).orElse(null);
      }
    };
  }

  private static EncodedIdService buildNoopEncodedIdService() {
    return new EncodedIdService() {};
  }
}
