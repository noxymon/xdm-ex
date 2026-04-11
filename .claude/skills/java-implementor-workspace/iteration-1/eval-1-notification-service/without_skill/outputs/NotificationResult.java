/**
 * Represents the result of sending a notification through a single channel.
 */
public class NotificationResult {

    private final String channelName;
    private final boolean success;
    private final String errorMessage;

    private NotificationResult(String channelName, boolean success, String errorMessage) {
        this.channelName = channelName;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a successful result for the given channel.
     */
    public static NotificationResult success(String channelName) {
        return new NotificationResult(channelName, true, null);
    }

    /**
     * Creates a failure result for the given channel with an error message.
     */
    public static NotificationResult failure(String channelName, String errorMessage) {
        return new NotificationResult(channelName, false, errorMessage);
    }

    public String getChannelName() {
        return channelName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (success) {
            return "NotificationResult{channel='" + channelName + "', success=true}";
        }
        return "NotificationResult{channel='" + channelName + "', success=false, error='" + errorMessage + "'}";
    }
}
