import java.util.Objects;

/**
 * Immutable configuration for the push notification channel.
 *
 * <p>Holds credentials for a push notification service (e.g., Firebase Cloud Messaging, APNs).
 */
public final class PushConfig {

    private final String serverKey;
    private final String projectId;
    private final String serviceEndpoint;

    private PushConfig(Builder builder) {
        this.serverKey = builder.serverKey;
        this.projectId = builder.projectId;
        this.serviceEndpoint = builder.serviceEndpoint;
    }

    public static Builder builder(String serverKey, String projectId) {
        return new Builder(serverKey, projectId);
    }

    public String serverKey() { return serverKey; }
    public String projectId() { return projectId; }
    public String serviceEndpoint() { return serviceEndpoint; }

    @Override
    public String toString() {
        return "PushConfig[projectId=%s, endpoint=%s]".formatted(projectId, serviceEndpoint);
    }

    public static final class Builder {

        private final String serverKey;
        private final String projectId;
        private String serviceEndpoint = "https://fcm.googleapis.com/fcm/send";

        private Builder(String serverKey, String projectId) {
            this.serverKey = Objects.requireNonNull(serverKey, "serverKey must not be null");
            this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");
        }

        public Builder serviceEndpoint(String serviceEndpoint) {
            this.serviceEndpoint = Objects.requireNonNull(serviceEndpoint, "serviceEndpoint must not be null");
            return this;
        }

        public PushConfig build() {
            return new PushConfig(this);
        }
    }
}
