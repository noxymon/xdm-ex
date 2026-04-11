/**
 * Notification channel that sends messages via SMS using an external API.
 *
 * <p>In production, this would make HTTP requests to an SMS gateway (e.g., Twilio,
 * Vonage). The implementation here demonstrates the structure.</p>
 */
public class SmsNotificationChannel implements NotificationChannel {

    private final SmsConfig config;

    public SmsNotificationChannel(SmsConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("SmsConfig must not be null");
        }
        this.config = config;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }

    @Override
    public void send(String recipient, String subject, String body) throws NotificationException {
        if (recipient == null || recipient.isBlank()) {
            throw new NotificationException(getChannelName(), "Recipient phone number is required");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException(getChannelName(), "SMS body is required");
        }

        try {
            // In production, this would make an HTTP POST to the SMS API:
            //   HttpClient client = HttpClient.newHttpClient();
            //   String payload = buildJsonPayload(config.getFromNumber(), recipient, body);
            //   HttpRequest request = HttpRequest.newBuilder()
            //       .uri(URI.create(config.getApiBaseUrl() + "/messages"))
            //       .header("Authorization", "Bearer " + config.getApiKey())
            //       .header("Content-Type", "application/json")
            //       .POST(HttpRequest.BodyPublishers.ofString(payload))
            //       .build();
            //   client.send(request, HttpResponse.BodyHandlers.ofString());

            // SMS typically combines subject and body into a single message
            String message = (subject != null && !subject.isBlank())
                    ? subject + ": " + body
                    : body;

            System.out.println("[SMS] Sending to " + recipient + " from " + config.getFromNumber());
            System.out.println("[SMS] Message: " + message);
        } catch (Exception e) {
            throw new NotificationException(getChannelName(),
                    "Failed to send SMS to " + recipient + ": " + e.getMessage(), e);
        }
    }
}
