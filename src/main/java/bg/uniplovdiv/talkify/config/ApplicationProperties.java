package bg.uniplovdiv.talkify.config;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("application")
@Data
@FieldDefaults(level = PRIVATE)
public class ApplicationProperties {

  String encodedIdKey;

  @NestedConfigurationProperty Headers headers;
  @NestedConfigurationProperty Mail mail;

  @Data
  @FieldDefaults(level = PRIVATE)
  public static class Headers {
    String contentSecurityPolicy;
  }

  @Data
  @FieldDefaults(level = PRIVATE)
  public static class Mail {
    boolean enabled;
    String rewriteSendTo;
    String bcc;
    String senderName;

    public boolean isEnabled() {
      return enabled && isNotBlank(senderName);
    }

    public boolean isBccEnabled() {
      return isNotBlank(bcc);
    }

    public boolean isRewriteSendToEnabled() {
      return isNotBlank(rewriteSendTo);
    }
  }
}
