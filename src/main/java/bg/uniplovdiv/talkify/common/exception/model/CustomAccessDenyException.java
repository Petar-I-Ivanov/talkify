package bg.uniplovdiv.talkify.common.exception.model;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.utils.constants.LocalizedMessages;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;

@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomAccessDenyException extends AccessDeniedException {

  private static final long serialVersionUID = -7103322087506647448L;

  LocalizedMessages customMessage;

  public CustomAccessDenyException(LocalizedMessages msg) {
    super(msg.getKey());
    this.customMessage = msg;
  }
}
