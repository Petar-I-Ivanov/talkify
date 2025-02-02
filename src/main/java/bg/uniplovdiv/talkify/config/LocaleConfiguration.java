package bg.uniplovdiv.talkify.config;

import static java.util.Locale.ENGLISH;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfiguration {

  @Bean
  public LocaleResolver localeResolver() {
    var localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(ENGLISH);
    return localeResolver;
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    var bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }
}
