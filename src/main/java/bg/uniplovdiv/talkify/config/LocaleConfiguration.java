package bg.uniplovdiv.talkify.config;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofHours;
import static java.util.Locale.ENGLISH;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfiguration {

  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/messages");
    messageSource.setDefaultEncoding(UTF_8.name());
    messageSource.setCacheSeconds((int) ofHours(1).toSeconds());
    return messageSource;
  }

  @Bean
  public LocaleResolver localeResolver() {
    var localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(ENGLISH);
    return localeResolver;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    var bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }
}
