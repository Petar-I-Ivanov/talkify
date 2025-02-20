package bg.uniplovdiv.talkify.common.exception;

import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.NOT_FOUND_EXC;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import bg.uniplovdiv.talkify.common.exception.model.CustomAccessDenyException;
import bg.uniplovdiv.talkify.common.exception.model.DataValidationException;
import bg.uniplovdiv.talkify.common.exception.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GenericApiExceptionHandler {

  MessageSource messageSource;

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ErrorResponse handleEntityNotFound(EntityNotFoundException ex, Locale locale) {
    log.error(ex.getMessage(), ex);
    var localizedMessage = messageSource.getMessage(NOT_FOUND_EXC.getKey(), null, locale);
    return new ErrorResponse(localizedMessage, NOT_FOUND.value());
  }

  @ExceptionHandler(DataValidationException.class)
  @ResponseStatus(BAD_REQUEST)
  public ErrorResponse handleDataValidationException(DataValidationException ex, Locale locale) {
    log.error(ex.getMessage(), ex);
    var localizedMessage = messageSource.getMessage(ex.getCustomMessage().getKey(), null, locale);
    return new ErrorResponse(localizedMessage, BAD_REQUEST.value());
  }

  @ExceptionHandler(CustomAccessDenyException.class)
  @ResponseStatus(FORBIDDEN)
  public ErrorResponse handleCustomAccessDenyException(
      CustomAccessDenyException ex, Locale locale) {
    log.error(ex.getMessage(), ex);
    var localizedMessage = messageSource.getMessage(ex.getCustomMessage().getKey(), null, locale);
    return new ErrorResponse(localizedMessage, FORBIDDEN.value());
  }
}
