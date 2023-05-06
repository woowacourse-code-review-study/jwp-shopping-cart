package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends HttpException {

    public AuthenticationException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
