import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service that dispatches notifications across multiple channels.
 *
 * <p>Channels are registered at runtime via {@link #addChannel(NotificationChannel)}.
 * When {@link #send(String, String, String)} or {@link #send(String, String, String, List)}
 * is called, the service iterates through the requested channels, attempts delivery on each,
 * and collects results. A failure on one channel does not prevent the others from executing.</p>
 *
 * <p>To add a new notification channel in the future, implement {@link NotificationChannel}
 * and register it with this service -- no changes to existing code are required.</p>
 */
public class NotificationService {

    private final List<NotificationChannel> channels = new CopyOnWriteArrayList<>();

    /**
     * Registers a new notification channel.
     *
     * @param channel the channel to add
     * @throws IllegalArgumentException if channel is null
     */
    public void addChannel(NotificationChannel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel must not be null");
        }
        channels.add(channel);
    }

    /**
     * Removes a previously registered notification channel.
     *
     * @param channel the channel to remove
     * @return true if the channel was found and removed
     */
    public boolean removeChannel(NotificationChannel channel) {
        return channels.remove(channel);
    }

    /**
     * Returns an unmodifiable view of the registered channels.
     */
    public List<NotificationChannel> getChannels() {
        return Collections.unmodifiableList(new ArrayList<>(channels));
    }

    /**
     * Sends a notification through all registered channels.
     * Failures on individual channels are captured in the returned results
     * and do not prevent other channels from sending.
     *
     * @param recipient the target recipient
     * @param subject   the notification subject/title
     * @param body      the notification body/content
     * @return a list of results, one per registered channel
     */
    public List<NotificationResult> send(String recipient, String subject, String body) {
        return send(recipient, subject, body, null);
    }

    /**
     * Sends a notification through the specified channels only.
     * If {@code channelNames} is null or empty, all registered channels are used.
     * Failures on individual channels are captured in the returned results
     * and do not prevent other channels from sending.
     *
     * @param recipient    the target recipient
     * @param subject      the notification subject/title
     * @param body         the notification body/content
     * @param channelNames the names of channels to use, or null/empty for all
     * @return a list of results, one per attempted channel
     */
    public List<NotificationResult> send(String recipient, String subject, String body,
                                         List<String> channelNames) {
        List<NotificationResult> results = new ArrayList<>();
        List<NotificationChannel> targetChannels = resolveChannels(channelNames);

        if (targetChannels.isEmpty()) {
            System.err.println("NotificationService: No channels available to send notification.");
            return results;
        }

        for (NotificationChannel channel : targetChannels) {
            try {
                channel.send(recipient, subject, body);
                results.add(NotificationResult.success(channel.getChannelName()));
            } catch (NotificationException e) {
                System.err.println("NotificationService: Channel " + channel.getChannelName()
                        + " failed: " + e.getMessage());
                results.add(NotificationResult.failure(channel.getChannelName(), e.getMessage()));
            } catch (Exception e) {
                // Catch any unexpected exception so other channels can still proceed
                System.err.println("NotificationService: Unexpected error on channel "
                        + channel.getChannelName() + ": " + e.getMessage());
                results.add(NotificationResult.failure(channel.getChannelName(),
                        "Unexpected error: " + e.getMessage()));
            }
        }

        return results;
    }

    /**
     * Resolves which channels to use based on the requested channel names.
     * If names is null or empty, returns all registered channels.
     */
    private List<NotificationChannel> resolveChannels(List<String> channelNames) {
        if (channelNames == null || channelNames.isEmpty()) {
            return new ArrayList<>(channels);
        }

        List<NotificationChannel> resolved = new ArrayList<>();
        for (NotificationChannel channel : channels) {
            if (channelNames.contains(channel.getChannelName())) {
                resolved.add(channel);
            }
        }
        return resolved;
    }
}
