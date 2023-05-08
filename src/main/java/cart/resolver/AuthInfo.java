package cart.resolver;

public class AuthInfo {

    private final String email;

    public AuthInfo(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
