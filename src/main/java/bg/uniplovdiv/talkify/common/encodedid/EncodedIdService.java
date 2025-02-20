package bg.uniplovdiv.talkify.common.encodedid;

import static at.favre.lib.idmask.Cache.SimpleLruMemCache;
import static at.favre.lib.idmask.KeyManager.Factory;

import at.favre.lib.bytes.Bytes;
import at.favre.lib.idmask.Config;
import at.favre.lib.idmask.Config.Builder;
import at.favre.lib.idmask.IdMask;
import at.favre.lib.idmask.IdMasks;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public interface EncodedIdService {

  default String encode(Long internalId) {
    return Optional.ofNullable(internalId).map(String::valueOf).orElse(null);
  }

  default Long decode(String publicId) {
    return Optional.ofNullable(publicId).map(Long::parseLong).orElse(null);
  }

  static EncodedIdService getInstance(String key) {
    return Optional.ofNullable(key)
        .map(EncodedIdService::buildMask)
        .map(EncodedIdService::buildMaskingInstance)
        .orElseGet(EncodedIdService::buildNoopInstance);
  }

  private static IdMask<Long> buildMask(String key) {
    return Optional.ofNullable(key)
        .filter(StringUtils::isNotBlank)
        .map(Bytes::parseHex)
        .map(Bytes::array)
        .map(Factory::with)
        .map(Config::builder)
        .map(builder -> builder.enableCache(true).cacheImpl(new SimpleLruMemCache(4096)))
        .map(Builder::build)
        .map(IdMasks::forLongIds)
        .orElse(null);
  }

  private static EncodedIdService buildMaskingInstance(IdMask<Long> masker) {
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

  private static EncodedIdService buildNoopInstance() {
    return new EncodedIdService() {};
  }
}
