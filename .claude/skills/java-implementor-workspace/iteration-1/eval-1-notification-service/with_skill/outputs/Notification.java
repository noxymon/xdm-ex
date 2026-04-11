import java.time.Instant;
import java.util.Objects;

/**
 * An immutable notification message to be delivered through one or more channels.
 *
 * <p>Use the {@link Builder} to construct instances:
 * <pre>{@code
 * Notification notification = Notification.builder("user@example.com", "Server Alert", "CPU usage exceeded 90%")
 *         .timestamp(Instant.now())
 *         .build();
 * }</pre>
 */
public final class Notification {

    private final String recipient;
    private final String subject;
    private final String body;
    private final Instant timestamp;

    private Notification(Builder builder) {
        this.recipient = builder.recipient;
        this.subject = builder.subject;
        this.body = builder.body;
        this.timestamp = builder.timestamp;
    }

    /**
     * Returns a new builder for constructing a {@code Notification}.
     *
     * @param recipient the target recipient identifier (email, phone number, device token, etc.)
     * @param subject   the notification subject or title
     * @param body      the notification body text
     * @return a new builder instance
     * @throws NullPointerException if any argument is null
     */
    public static Builder builder(String recipient, String subject, String body) {
        return new Builder(recipient, subject, body);
    }

    public String recipient() {
        return recipient;
    }

    public String subject() {
        return subject;
    }

    public String body() {
        return body;
    }

    public Instant timestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;
        return recipient.equals(that.recipient)
                && subject.equals(that.subject)
                && body.equals(that.body)
                && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipient, subject, body, timestamp);
    }

    @Override
    public String toString() {
        return "Notification[recipient=%s, subject=%s, timestamp=%s]".formatted(recipient, subject, timestamp);
    }

    public static final class Builder {

        private final String recipient;
        private final String subject;
        private final String body;
        private Instant timestamp = Instant.now();

        private Builder(String recipient, String subject, String body) {
            this.recipient = Objects.requireNonNull(recipient, "recipient must not be null");
            this.subject = Objects.requireNonNull(subject, "subject must not be null");
            this.body = Objects.requireNonNull(body, "body must not be null");
        }

        /**
         * Sets the notification timestamp. Defaults to {@link Instant#now()} if not specified.
         */
        public Builder timestamp(Instant timestamp) {
            this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
