package JavaPRO.config.exception;

import JavaPRO.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestHandler(BadRequestException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getErrorDescription()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationHandler(AuthenticationException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getErrorDescription()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getError(),
                        exception.getErrorDescription()),
                HttpStatus.NOT_FOUND);
    }
}
