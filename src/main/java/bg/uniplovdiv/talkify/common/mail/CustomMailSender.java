package bg.uniplovdiv.talkify.common.mail;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

import bg.uniplovdiv.talkify.common.mail.model.MailPropsBuilder.MailProps;
import bg.uniplovdiv.talkify.config.ApplicationProperties;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class CustomMailSender {

  ApplicationProperties applicationProperties;
  MessageSource messageSource;

  Configuration freeMakerConfig;
  JavaMailSender mailSender;

  public boolean sendEmail(MailProps mailProps) {
    var sendTo = mailProps.getSendTo().stream().findFirst().orElseThrow();
    var preparator = buildMessagePreparator(mailProps, sendTo);

    if (preparator == null) {
      log.debug("Sending message to {} has failed. Please, try again.", sendTo);
      return false;
    }

    if (!applicationProperties.getMail().isEnabled()) {
      log.debug("Mail sending is disabled by 'application.mail.enabled' property.");
      return false;
    }

    try {
      mailSender.send(preparator);
      log.debug("Message has been sent successfully to {}", sendTo);
    } catch (MailException me) {
      log.error("Sending message to {} failed: {}", sendTo, me.getMessage());
      return false;
    }

    return true;
  }

  private MimeMessagePreparator buildMessagePreparator(MailProps props, String sendTo) {
    var appProps = applicationProperties.getMail();
    var localizedSubject =
        messageSource.getMessage(
            props.getSubject().getSubject(), props.getSubjectParams(), props.getLocale());

    return mimeMessage -> {
      var helper = new MimeMessageHelper(mimeMessage, true, UTF_8.name());
      helper.setSubject(localizedSubject);
      helper.setFrom(appProps.getSenderName());

      if (appProps.isBccEnabled() && props.isBcc()) {
        helper.addBcc(appProps.getBcc());
      }

      if (appProps.isRewriteSendToEnabled()) {
        log.debug("Mail recepient rewritten to {}.", appProps.getRewriteSendTo());
        helper.setTo(appProps.getRewriteSendTo());
      } else {
        helper.setTo(sendTo);
      }

      if (nonNull(props.getEmailBody())) {
        helper.setText(props.getEmailBody());
      } else {
        var template = freeMakerConfig.getTemplate(props.getTemplate());
        var body = processTemplateIntoString(template, props.getVariables());
        helper.setText(body, true);
      }
    };
  }
}
