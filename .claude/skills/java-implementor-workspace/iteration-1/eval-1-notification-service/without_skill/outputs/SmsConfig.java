/**
 * Configuration for SMS notification delivery via an external API (e.g., Twilio).
 */
public class SmsConfig {

    private final String apiKey;
    private final String apiSecret;
    private final String fromNumber;
    private final String apiBaseUrl;

    public SmsConfig(String apiKey, String apiSecret, String fromNumber, String apiBaseUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.fromNumber = fromNumber;
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
