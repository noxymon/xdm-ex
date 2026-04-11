import java.util.Objects;

/**
 * Sends notifications via email using SMTP.
 *
 * <p>This channel uses the provided {@link SmtpConfig} for all connection details.
 * The recipient field of the {@link Notification} is treated as the destination email address.
 */
public final class EmailNotificationChannel implements NotificationChannel {

    private static final String CHANNEL_NAME = "Email";

    private final SmtpConfig config;

    /**
     * Creates an email channel with the given SMTP configuration.
     *
     * @param config SMTP server settings
     * @throws NullPointerException if config is null
     */
    public EmailNotificationChannel(SmtpConfig config) {
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    /**
     * Static factory method for creating an email channel.
     *
     * @param config SMTP server settings
     * @return a new email notification channel
     */
    public static EmailNotificationChannel of(SmtpConfig config) {
        return new EmailNotificationChannel(config);
    }

    @Override
    public void send(Notification notification) throws NotificationException {
        Objects.requireNonNull(notification, "notification must not be null");
        try {
            // In a real implementation, this would use JavaMail or a similar library
            // to connect to the SMTP server and send the email.
            //
            // Example flow:
            // 1. Create a mail session with config.host(), config.port(), config.useTls()
            // 2. Authenticate with config.username(), config.password() if provided
            // 3. Compose message from config.fromAddress() to notification.recipient()
            // 4. Set subject and body
            // 5. Send via Transport
            doSend(notification);
        } catch (NotificationException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificationException(CHANNEL_NAME,
                    "Failed to send email to " + notification.recipient(), e);
        }
    }

    @Override
    public String channelName() {
        return CHANNEL_NAME;
    }

    /**
     * Performs the actual email sending. Extracted to allow subclassing in tests
     * or replacement with a real SMTP client.
     */
    protected void doSend(Notification notification) throws Exception {
        // Placeholder for actual SMTP sending logic.
        // A production implementation would integrate with javax.mail or Jakarta Mail.
        System.out.printf("[%s] Sending email via %s from %s to %s: %s%n",
                CHANNEL_NAME, config.host(), config.fromAddress(),
                notification.recipient(), notification.subject());
    }
}
