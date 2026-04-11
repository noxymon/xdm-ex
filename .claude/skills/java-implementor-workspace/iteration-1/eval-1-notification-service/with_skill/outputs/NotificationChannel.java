/**
 * Strategy interface for a notification delivery channel.
 *
 * <p>Each implementation handles a specific delivery mechanism (email, SMS, push, etc.)
 * and encapsulates its own configuration. New channels can be added by implementing
 * this interface without modifying any existing code.
 */
public interface NotificationChannel {

    /**
     * Sends the given notification through this channel.
     *
     * @param notification the notification to send; never null
     * @throws NotificationException if delivery fails
     */
    void send(Notification notification) throws NotificationException;

    /**
     * Returns a human-readable name for this channel, used in logging and error reporting.
     *
     * @return the channel name, e.g. "Email", "SMS", "Push"
     */
    String channelName();
}
