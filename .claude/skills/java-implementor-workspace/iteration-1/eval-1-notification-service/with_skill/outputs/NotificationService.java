import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Dispatches notifications across multiple {@link NotificationChannel channels}.
 *
 * <p>When sending, this service attempts delivery through every registered channel.
 * If one channel fails, the others still execute. The aggregate result is captured
 * in a {@link NotificationResult} that reports successes and failures.
 *
 * <p>New channels can be added at any time via {@link #registerChannel(NotificationChannel)}
 * or at construction time, without modifying this class (Open/Closed Principle).
 *
 * <p>Usage example:
 * <pre>{@code
 * NotificationService service = NotificationService.of(
 *         EmailNotificationChannel.of(smtpConfig),
 *         SmsNotificationChannel.of(smsConfig),
 *         PushNotificationChannel.of(pushConfig)
 * );
 *
 * Notification notification = Notification.builder("user@example.com", "Alert", "Details...")
 *         .build();
 *
 * NotificationResult result = service.send(notification);
 * if (!result.isFullSuccess()) {
 *     result.failures().forEach(f -> log.warn("Channel failed: {}", f));
 * }
 * }</pre>
 */
public final class NotificationService {

    private final List<NotificationChannel> channels;

    private NotificationService(List<NotificationChannel> channels) {
        this.channels = new ArrayList<>(channels);
    }

    /**
     * Creates a service with the given channels.
     *
     * @param channels one or more notification channels
     * @return a new notification service
     * @throws IllegalArgumentException if no channels are provided
     */
    public static NotificationService of(NotificationChannel... channels) {
        Objects.requireNonNull(channels, "channels must not be null");
        if (channels.length == 0) {
            throw new IllegalArgumentException("At least one notification channel is required");
        }
        List<NotificationChannel> channelList = new ArrayList<>(channels.length);
        for (NotificationChannel channel : channels) {
            channelList.add(Objects.requireNonNull(channel, "channel must not be null"));
        }
        return new NotificationService(channelList);
    }

    /**
     * Creates a service from a pre-built list of channels.
     *
     * @param channels the list of notification channels
     * @return a new notification service
     * @throws IllegalArgumentException if the list is empty
     */
    public static NotificationService fromChannels(List<NotificationChannel> channels) {
        Objects.requireNonNull(channels, "channels must not be null");
        if (channels.isEmpty()) {
            throw new IllegalArgumentException("At least one notification channel is required");
        }
        for (NotificationChannel channel : channels) {
            Objects.requireNonNull(channel, "channel must not be null");
        }
        return new NotificationService(channels);
    }

    /**
     * Registers an additional channel after construction.
     *
     * @param channel the channel to add
     * @throws NullPointerException if channel is null
     */
    public void registerChannel(NotificationChannel channel) {
        channels.add(Objects.requireNonNull(channel, "channel must not be null"));
    }

    /**
     * Sends the notification through all registered channels.
     *
     * <p>Each channel is attempted independently. A failure in one channel does not
     * prevent the others from executing. The returned {@link NotificationResult}
     * contains both the list of successful channel names and any failures.
     *
     * @param notification the notification to send
     * @return the result summarizing successes and failures across all channels
     */
    public NotificationResult send(Notification notification) {
        Objects.requireNonNull(notification, "notification must not be null");

        List<String> succeeded = new ArrayList<>();
        List<NotificationResult.ChannelFailure> failures = new ArrayList<>();

        for (NotificationChannel channel : channels) {
            try {
                channel.send(notification);
                succeeded.add(channel.channelName());
            } catch (NotificationException e) {
                failures.add(new NotificationResult.ChannelFailure(channel.channelName(), e));
            }
        }

        return new NotificationResult(succeeded, failures);
    }

    /**
     * Returns an unmodifiable view of the currently registered channels' names.
     */
    public List<String> registeredChannelNames() {
        return channels.stream()
                .map(NotificationChannel::channelName)
                .toList();
    }
}
