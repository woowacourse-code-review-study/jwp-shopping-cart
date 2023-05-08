package cart.controller;

import cart.dto.ErrorResponse;
import cart.exception.AlreadyAddedProductException;
import cart.exception.CartNotFoundException;
import cart.exception.ProductInCartDeleteException;
import cart.exception.ProductNotFoundException;
import cart.exception.UserNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error("예상치 못한 예외가 발생했습니다.", exception);
        return ResponseEntity.internalServerError().body(new ErrorResponse("예상치 못한 예외가 발생했습니다."));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.warn("잘못된 인자가 들어왔습니다", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({CartNotFoundException.class, ProductNotFoundException.class, UserNotFoundException.class})
    private ResponseEntity<ErrorResponse> handleNotFoundException(final Exception exception) {
        log.warn("존재하지 않는 리소스에 접근했습니다.", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleAlreadyAddedProduct(final AlreadyAddedProductException e) {
        log.warn("이미 장바구니에 담긴 상품입니다.", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleProductInCartDelete(final ProductInCartDeleteException e) {
        log.warn("장바구니에 담긴 상품을 삭제할 수 없습니다.", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log.warn("유효성 검사에 실패했습니다.", exception);
        final Map<String, String> body = exception.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return handleExceptionInternal(exception, body, headers, status, request);
    }
}
