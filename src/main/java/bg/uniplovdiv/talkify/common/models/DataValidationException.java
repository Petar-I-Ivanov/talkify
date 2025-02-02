package bg.uniplovdiv.talkify.common.models;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.utils.constants.LocalizedMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DataValidationException extends RuntimeException {

  private static final long serialVersionUID = -4730484141342973914L;

  LocalizedMessages customMessage;

  // move from here
  public static final void throwIfCondition(boolean isCondition, LocalizedMessages message) {
    if (isCondition) {
      throw new DataValidationException(message);
    }
  }
}
