/**
 * Configuration for push notification delivery (e.g., Firebase Cloud Messaging, APNs).
 */
public class PushConfig {

    private final String serverKey;
    private final String apiEndpoint;

    public PushConfig(String serverKey, String apiEndpoint) {
        this.serverKey = serverKey;
        this.apiEndpoint = apiEndpoint;
    }

    public String getServerKey() {
        return serverKey;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }
}
