package bg.uniplovdiv.talkify.config;

import static lombok.AccessLevel.PRIVATE;

import at.favre.lib.idmask.IdMask;
import bg.uniplovdiv.talkify.common.encodedid.EncodedIdService;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = PRIVATE)
public class EncodedIdConfiguration {

  @Bean
  public EncodedIdService encodedIdService(ApplicationProperties properties) {
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
