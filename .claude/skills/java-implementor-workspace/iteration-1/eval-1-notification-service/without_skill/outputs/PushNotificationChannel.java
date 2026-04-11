/**
 * Notification channel that sends push notifications to mobile/desktop devices.
 *
 * <p>In production, this would integrate with Firebase Cloud Messaging (FCM),
 * Apple Push Notification Service (APNs), or a similar push provider.
 * The recipient parameter represents a device token.</p>
 */
public class PushNotificationChannel implements NotificationChannel {

    private final PushConfig config;

    public PushNotificationChannel(PushConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("PushConfig must not be null");
        }
        this.config = config;
    }

    @Override
    public String getChannelName() {
        return "PUSH";
    }

    @Override
    public void send(String recipient, String subject, String body) throws NotificationException {
        if (recipient == null || recipient.isBlank()) {
            throw new NotificationException(getChannelName(), "Device token is required");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException(getChannelName(), "Push notification body is required");
        }

        try {
            // In production, this would post to a push notification service:
            //   HttpClient client = HttpClient.newHttpClient();
            //   String payload = buildFcmPayload(recipient, subject, body);
            //   HttpRequest request = HttpRequest.newBuilder()
            //       .uri(URI.create(config.getApiEndpoint()))
            //       .header("Authorization", "key=" + config.getServerKey())
            //       .header("Content-Type", "application/json")
            //       .POST(HttpRequest.BodyPublishers.ofString(payload))
            //       .build();
            //   client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[PUSH] Sending to device " + recipient);
            System.out.println("[PUSH] Title: " + subject);
            System.out.println("[PUSH] Body: " + body);
        } catch (Exception e) {
            throw new NotificationException(getChannelName(),
                    "Failed to send push notification to " + recipient + ": " + e.getMessage(), e);
        }
    }
}
