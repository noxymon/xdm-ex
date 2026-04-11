/**
 * Indicates that a notification could not be delivered through a channel.
 *
 * <p>This is a checked exception because delivery failures are recoverable
 * conditions — the caller can log the failure and continue with other channels.
 */
public class NotificationException extends Exception {

    private final String channelName;

    /**
     * @param channelName the name of the channel that failed
     * @param message     a description of the failure
     */
    public NotificationException(String channelName, String message) {
        super("Channel '%s' failed: %s".formatted(channelName, message));
        this.channelName = channelName;
    }

    /**
     * @param channelName the name of the channel that failed
     * @param message     a description of the failure
     * @param cause       the underlying exception
     */
    public NotificationException(String channelName, String message, Throwable cause) {
        super("Channel '%s' failed: %s".formatted(channelName, message), cause);
        this.channelName = channelName;
    }

    /**
     * Returns the name of the channel that produced this exception.
     */
    public String channelName() {
        return channelName;
    }
}
