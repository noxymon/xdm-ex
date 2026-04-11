import java.util.List;
import java.util.Objects;

/**
 * Immutable result of a multi-channel notification dispatch.
 *
 * <p>Contains the list of channels that succeeded and the list of failures,
 * allowing callers to inspect partial success scenarios.
 */
public final class NotificationResult {

    private final List<String> succeededChannels;
    private final List<ChannelFailure> failures;

    NotificationResult(List<String> succeededChannels, List<ChannelFailure> failures) {
        this.succeededChannels = List.copyOf(Objects.requireNonNull(succeededChannels));
        this.failures = List.copyOf(Objects.requireNonNull(failures));
    }

    /**
     * Returns the names of channels that delivered the notification successfully.
     */
    public List<String> succeededChannels() {
        return succeededChannels;
    }

    /**
     * Returns details of channels that failed to deliver.
     */
    public List<ChannelFailure> failures() {
        return failures;
    }

    /**
     * Returns {@code true} if all channels delivered successfully.
     */
    public boolean isFullSuccess() {
        return failures.isEmpty();
    }

    /**
     * Returns {@code true} if at least one channel delivered successfully.
     */
    public boolean isPartialSuccess() {
        return !succeededChannels.isEmpty();
    }

    /**
     * Returns {@code true} if every channel failed.
     */
    public boolean isCompleteFailure() {
        return succeededChannels.isEmpty() && !failures.isEmpty();
    }

    @Override
    public String toString() {
        return "NotificationResult[succeeded=%s, failed=%s]".formatted(succeededChannels, failures);
    }

    /**
     * Details of a single channel failure.
     */
    public record ChannelFailure(String channelName, NotificationException exception) {

        public ChannelFailure {
            Objects.requireNonNull(channelName, "channelName must not be null");
            Objects.requireNonNull(exception, "exception must not be null");
        }

        @Override
        public String toString() {
            return "ChannelFailure[channel=%s, message=%s]".formatted(channelName, exception.getMessage());
        }
    }
}
