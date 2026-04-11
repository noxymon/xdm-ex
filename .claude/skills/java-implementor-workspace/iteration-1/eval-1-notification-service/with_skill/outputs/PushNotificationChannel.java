import java.util.Objects;

/**
 * Sends notifications via push notification services (e.g., Firebase Cloud Messaging).
 *
 * <p>The recipient field of the {@link Notification} is treated as the device token.
 */
public final class PushNotificationChannel implements NotificationChannel {

    private static final String CHANNEL_NAME = "Push";

    private final PushConfig config;

    public PushNotificationChannel(PushConfig config) {
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    public static PushNotificationChannel of(PushConfig config) {
        return new PushNotificationChannel(config);
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
                    "Failed to send push notification to device " + notification.recipient(), e);
        }
    }

    @Override
    public String channelName() {
        return CHANNEL_NAME;
    }

    /**
     * Performs the actual push notification sending.
     */
    protected void doSend(Notification notification) throws Exception {
        // Placeholder for actual push notification logic.
        // A production implementation would:
        // 1. Build an HTTP request to config.serviceEndpoint()
        // 2. Authenticate with config.serverKey()
        // 3. Target the device token in notification.recipient()
        // 4. Set title from notification.subject(), body from notification.body()
        // 5. Send and verify delivery status
        System.out.printf("[%s] Sending push to device %s via project %s: %s%n",
                CHANNEL_NAME, notification.recipient(),
                config.projectId(), notification.subject());
    }
}
