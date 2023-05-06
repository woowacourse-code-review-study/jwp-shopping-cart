package cart.exception;

public final class ErrorResponseDto {

    private final String message;

    public ErrorResponseDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
