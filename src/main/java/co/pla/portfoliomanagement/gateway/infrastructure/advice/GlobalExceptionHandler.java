package co.pla.portfoliomanagement.gateway.infrastructure.advice;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;
import co.pla.portfoliomanagement.identity.application.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.identity.application.exceptions.UserNotFoundException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.InvalidPositionUpdateException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.PortfolioException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.PortfolioNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import org.quartz.SchedulerException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {PortfolioNotFoundException.class, UserNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
        ex.printStackTrace();
        int status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), status));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        int status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), status));
    }

    @ExceptionHandler(value = {SchedulerException.class, InvalidPositionUpdateException.class, DataIntegrityViolationException.class, IllegalArgumentException.class, PortfolioException.class})
    protected ResponseEntity<Object> handlePortfolioException(Exception ex) {
        ex.printStackTrace();
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), status));
    }

    @ExceptionHandler(value = {ExpiredJwtException.class, InvalidPasswordException.class})
    protected ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        ex.printStackTrace();
        int status = HttpStatus.UNAUTHORIZED.value();
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), status));
    }

    @ExceptionHandler(value = {RuntimeException.class, BaseApplicationException.class})
    protected ResponseEntity<Object> handleBaseApplicationException(Exception ex) {
        ex.printStackTrace();
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), status));
    }


    @Getter
    @Setter
    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final Instant timestamp;

        public ErrorResponse(String message, int status) {
            this.status = status;
            this.message = message;
            this.timestamp = Instant.now();
        }

    }
}