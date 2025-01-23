package bg.uniplovdiv.talkify.common.advicer;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import bg.uniplovdiv.talkify.common.models.DataValidationException;
import bg.uniplovdiv.talkify.common.models.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GenericApiExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return new ErrorResponse(ex.getMessage(), NOT_FOUND.value());
  }

  @ExceptionHandler(DataValidationException.class)
  @ResponseStatus(BAD_REQUEST)
  public ErrorResponse handleDataValidationException(DataValidationException ex) {
    log.error(ex.getMessage(), ex);
    return new ErrorResponse(ex.getMessage(), BAD_REQUEST.value());
  }
}
