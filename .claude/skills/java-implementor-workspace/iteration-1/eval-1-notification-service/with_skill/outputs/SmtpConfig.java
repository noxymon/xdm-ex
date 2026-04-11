import java.util.Objects;

/**
 * Immutable SMTP configuration for the email notification channel.
 *
 * <p>Use the {@link Builder} to construct instances:
 * <pre>{@code
 * SmtpConfig config = SmtpConfig.builder("smtp.example.com", "noreply@example.com")
 *         .port(587)
 *         .useTls(true)
 *         .credentials("user", "password")
 *         .build();
 * }</pre>
 */
public final class SmtpConfig {

    private final String host;
    private final int port;
    private final boolean useTls;
    private final String fromAddress;
    private final String username;
    private final String password;

    private SmtpConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.useTls = builder.useTls;
        this.fromAddress = builder.fromAddress;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static Builder builder(String host, String fromAddress) {
        return new Builder(host, fromAddress);
    }

    public String host() { return host; }
    public int port() { return port; }
    public boolean useTls() { return useTls; }
    public String fromAddress() { return fromAddress; }
    public String username() { return username; }
    public String password() { return password; }

    @Override
    public String toString() {
        return "SmtpConfig[host=%s, port=%d, from=%s, tls=%s]".formatted(host, port, fromAddress, useTls);
    }

    public static final class Builder {

        private final String host;
        private final String fromAddress;
        private int port = 587;
        private boolean useTls = true;
        private String username = "";
        private String password = "";

        private Builder(String host, String fromAddress) {
            this.host = Objects.requireNonNull(host, "host must not be null");
            this.fromAddress = Objects.requireNonNull(fromAddress, "fromAddress must not be null");
        }

        public Builder port(int port) {
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("port must be between 1 and 65535, got: " + port);
            }
            this.port = port;
            return this;
        }

        public Builder useTls(boolean useTls) {
            this.useTls = useTls;
            return this;
        }

        public Builder credentials(String username, String password) {
            this.username = Objects.requireNonNull(username, "username must not be null");
            this.password = Objects.requireNonNull(password, "password must not be null");
            return this;
        }

        public SmtpConfig build() {
            return new SmtpConfig(this);
        }
    }
}
