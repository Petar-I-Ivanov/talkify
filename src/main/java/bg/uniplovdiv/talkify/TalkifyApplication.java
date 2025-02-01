package bg.uniplovdiv.talkify;

import bg.uniplovdiv.talkify.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties({ApplicationProperties.class})
public class TalkifyApplication {

  public static void main(String[] args) {
    SpringApplication.run(TalkifyApplication.class, args);
  }
}
