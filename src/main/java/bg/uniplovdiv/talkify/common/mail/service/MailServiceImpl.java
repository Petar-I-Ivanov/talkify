package bg.uniplovdiv.talkify.common.mail.service;

import static bg.uniplovdiv.talkify.common.mail.model.MailPropsBuilder.mailPropsBuilder;
import static bg.uniplovdiv.talkify.common.mail.model.MailVariable.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.common.mail.model.MailSubject;
import bg.uniplovdiv.talkify.common.mail.model.MailTemplate;
import bg.uniplovdiv.talkify.config.ApplicationProperties;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {

  static final String TALKIFY = "Talkify";

  ApplicationProperties properties;
  CustomMailSender mailSender;

  @Async
  @Override
  public Future<Boolean> sendMailForRegisteredFriend(
      String registererEmail, User registered, String password) {
    var mailProps =
        mailPropsBuilder()
            .sendTo(registered.getEmail())
            .template(MailTemplate.FRIEND_REGISTER_NOTIFY)
            .subject(MailSubject.FRIEND_REGISTER_NOTIFY)
            .addSubjectParam(TALKIFY)
            .addVariable(RECIEVER, registered.getEmail())
            .addVariable(FRIEND_USERNAME, registererEmail)
            .addVariable(APP_NAME, TALKIFY)
            .addVariable(USERNAME, registered.getUsername())
            .addVariable(PASSWORD, password)
            .addVariable(LOGIN_URL, properties.getBaseUrl() + "/sign-in")
            .build();
    return supplyAsync(() -> mailSender.sendEmail(mailProps));
  }
}
