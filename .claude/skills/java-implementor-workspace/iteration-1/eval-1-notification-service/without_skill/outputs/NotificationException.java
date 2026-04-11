/**
 * Exception thrown when a notification channel fails to send a message.
 */
public class NotificationException extends Exception {

    private final String channelName;

    public NotificationException(String channelName, String message) {
        super(message);
        this.channelName = channelName;
    }

    public NotificationException(String channelName, String message, Throwable cause) {
        super(message, cause);
        this.channelName = channelName;
    }

    /**
     * Returns the name of the channel that failed.
     */
    public String getChannelName() {
        return channelName;
    }
}
