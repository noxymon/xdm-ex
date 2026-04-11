import java.util.Objects;

/**
 * Sends notifications via SMS using an external gateway API.
 *
 * <p>The recipient field of the {@link Notification} is treated as the destination phone number.
 */
public final class SmsNotificationChannel implements NotificationChannel {

    private static final String CHANNEL_NAME = "SMS";

    private final SmsConfig config;

    public SmsNotificationChannel(SmsConfig config) {
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    public static SmsNotificationChannel of(SmsConfig config) {
        return new SmsNotificationChannel(config);
    }

    @Override
    public void send(Notification notification) throws NotificationException {
        Objects.requireNonNull(notification, "notification must not be null");
        try {
            doSend(notification);
        } catch (NotificationException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificationException(CHANNEL_NAME,
                    "Failed to send SMS to " + notification.recipient(), e);
        }
    }

    @Override
    public String channelName() {
        return CHANNEL_NAME;
    }

    /**
     * Performs the actual SMS sending via the gateway API.
     */
    protected void doSend(Notification notification) throws Exception {
        // Placeholder for actual SMS gateway HTTP call.
        // A production implementation would:
        // 1. Build an HTTP request to config.gatewayUrl()
        // 2. Authenticate with config.apiKey() and config.apiSecret()
        // 3. Set sender to config.senderPhoneNumber(), recipient to notification.recipient()
        // 4. Set message body (typically subject + body, truncated for SMS limits)
        // 5. Send request and check response status
        System.out.printf("[%s] Sending SMS from %s to %s via %s: %s%n",
                CHANNEL_NAME, config.senderPhoneNumber(),
                notification.recipient(), config.gatewayUrl(), notification.subject());
    }
}
