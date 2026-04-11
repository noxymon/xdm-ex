import java.util.Objects;

/**
 * Immutable configuration for the SMS notification channel.
 *
 * <p>Holds API credentials and sender information for an SMS gateway provider.
 */
public final class SmsConfig {

    private final String apiKey;
    private final String apiSecret;
    private final String senderPhoneNumber;
    private final String gatewayUrl;

    private SmsConfig(Builder builder) {
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.senderPhoneNumber = builder.senderPhoneNumber;
        this.gatewayUrl = builder.gatewayUrl;
    }

    public static Builder builder(String apiKey, String apiSecret, String senderPhoneNumber) {
        return new Builder(apiKey, apiSecret, senderPhoneNumber);
    }

    public String apiKey() { return apiKey; }
    public String apiSecret() { return apiSecret; }
    public String senderPhoneNumber() { return senderPhoneNumber; }
    public String gatewayUrl() { return gatewayUrl; }

    @Override
    public String toString() {
        return "SmsConfig[sender=%s, gateway=%s]".formatted(senderPhoneNumber, gatewayUrl);
    }

    public static final class Builder {

        private final String apiKey;
        private final String apiSecret;
        private final String senderPhoneNumber;
        private String gatewayUrl = "https://api.sms-gateway.example.com/v1/send";

        private Builder(String apiKey, String apiSecret, String senderPhoneNumber) {
            this.apiKey = Objects.requireNonNull(apiKey, "apiKey must not be null");
            this.apiSecret = Objects.requireNonNull(apiSecret, "apiSecret must not be null");
            this.senderPhoneNumber = Objects.requireNonNull(senderPhoneNumber, "senderPhoneNumber must not be null");
        }

        public Builder gatewayUrl(String gatewayUrl) {
            this.gatewayUrl = Objects.requireNonNull(gatewayUrl, "gatewayUrl must not be null");
            return this;
        }

        public SmsConfig build() {
            return new SmsConfig(this);
        }
    }
}
