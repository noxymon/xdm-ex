/**
 * Interface representing a notification channel (e.g., email, SMS, push).
 * Implement this interface to add new notification channels without
 * modifying existing code (Open/Closed Principle).
 */
public interface NotificationChannel {

    /**
     * Returns the name of this channel (e.g., "EMAIL", "SMS", "PUSH").
     */
    String getChannelName();

    /**
     * Sends a notification with the given subject and body.
     *
     * @param recipient the target recipient (email address, phone number, device token, etc.)
     * @param subject   the notification subject/title
     * @param body      the notification body/content
     * @throws NotificationException if sending fails
     */
    void send(String recipient, String subject, String body) throws NotificationException;
}
