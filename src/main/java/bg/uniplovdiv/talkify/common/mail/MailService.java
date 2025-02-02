package bg.uniplovdiv.talkify.common.mail;

import bg.uniplovdiv.talkify.auth.user.model.User;
import java.util.concurrent.Future;

public interface MailService {

  Future<Boolean> sendMailForRegisteredFriend(
      String registererEmail, User registered, String password);
}
