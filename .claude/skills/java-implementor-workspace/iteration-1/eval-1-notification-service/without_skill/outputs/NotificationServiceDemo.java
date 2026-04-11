import java.util.List;

/**
 * Demonstration of the NotificationService showing how to configure channels
 * and send notifications across multiple delivery methods.
 */
public class NotificationServiceDemo {

    public static void main(String[] args) {
        // 1. Create channel-specific configurations
        SmtpConfig smtpConfig = new SmtpConfig(
                "smtp.example.com", 587, "user@example.com", "password",
                "noreply@example.com", true
        );

        SmsConfig smsConfig = new SmsConfig(
                "api-key-12345", "api-secret-67890",
                "+1234567890", "https://api.smsprovider.com/v1"
        );

        PushConfig pushConfig = new PushConfig(
                "fcm-server-key-abc", "https://fcm.googleapis.com/fcm/send"
        );

        // 2. Create channels with their configurations
        NotificationChannel emailChannel = new EmailNotificationChannel(smtpConfig);
        NotificationChannel smsChannel = new SmsNotificationChannel(smsConfig);
        NotificationChannel pushChannel = new PushNotificationChannel(pushConfig);

        // 3. Build the service and register channels
        NotificationService service = new NotificationService();
        service.addChannel(emailChannel);
        service.addChannel(smsChannel);
        service.addChannel(pushChannel);

        // 4. Send to all channels at once
        System.out.println("=== Sending to ALL channels ===");
        List<NotificationResult> results = service.send(
                "user@example.com",
                "Welcome!",
                "Thank you for signing up."
        );
        printResults(results);

        // 5. Send to specific channels only
        System.out.println("\n=== Sending to EMAIL and PUSH only ===");
        results = service.send(
                "user@example.com",
                "Password Reset",
                "Click the link to reset your password.",
                List.of("EMAIL", "PUSH")
        );
        printResults(results);

        // 6. Demonstrate extensibility: adding a new channel at runtime
        System.out.println("\n=== Adding a custom Slack channel and sending ===");
        service.addChannel(new NotificationChannel() {
            @Override
            public String getChannelName() {
                return "SLACK";
            }

            @Override
            public void send(String recipient, String subject, String body)
                    throws NotificationException {
                System.out.println("[SLACK] Posting to #" + recipient + ": " + subject + " - " + body);
            }
        });

        results = service.send(
                "general",
                "Deployment Complete",
                "Version 2.0 has been deployed successfully."
        );
        printResults(results);
    }

    private static void printResults(List<NotificationResult> results) {
        System.out.println("--- Results ---");
        for (NotificationResult result : results) {
            System.out.println("  " + result);
        }
        long successCount = results.stream().filter(NotificationResult::isSuccess).count();
        System.out.println("  Total: " + results.size()
                + ", Succeeded: " + successCount
                + ", Failed: " + (results.size() - successCount));
    }
}
