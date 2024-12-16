package bg.uniplovdiv.talkify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TalkifyApplication {

  public static void main(String[] args) {
    SpringApplication.run(TalkifyApplication.class, args);
  }
}
