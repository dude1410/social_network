package javapro.config.exception;

import javapro.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestHandler(BadRequestException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationHandler(AuthenticationException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> interruptedHandler(ValidationException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ErrorResponse> interruptedHandler(InterruptedException exception) {
        return new ResponseEntity<>(
                new ErrorResponse("INTERRUPTED_EXCEPTION",
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> interruptedHandler(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(
                new ErrorResponse("HTTP_MESSAGE_NOT_READABLE_EXCEPTION",
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
