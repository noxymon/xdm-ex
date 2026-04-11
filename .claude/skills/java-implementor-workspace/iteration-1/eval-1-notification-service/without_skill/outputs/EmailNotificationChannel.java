/**
 * Notification channel that sends messages via email using SMTP.
 *
 * <p>In a production environment, this would use JavaMail (jakarta.mail) or a
 * similar library to connect to an SMTP server. The implementation here
 * demonstrates the structure and delegates to a simulated send operation.</p>
 */
public class EmailNotificationChannel implements NotificationChannel {

    private final SmtpConfig config;

    public EmailNotificationChannel(SmtpConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("SmtpConfig must not be null");
        }
        this.config = config;
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }

    @Override
    public void send(String recipient, String subject, String body) throws NotificationException {
        if (recipient == null || recipient.isBlank()) {
            throw new NotificationException(getChannelName(), "Recipient email address is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new NotificationException(getChannelName(), "Email subject is required");
        }

        try {
            // In production, this would create a MimeMessage and send via SMTP Transport.
            // For example:
            //   Properties props = new Properties();
            //   props.put("mail.smtp.host", config.getHost());
            //   props.put("mail.smtp.port", String.valueOf(config.getPort()));
            //   props.put("mail.smtp.starttls.enable", String.valueOf(config.isUseTls()));
            //   Session session = Session.getInstance(props, authenticator);
            //   MimeMessage message = new MimeMessage(session);
            //   message.setFrom(new InternetAddress(config.getFromAddress()));
            //   message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            //   message.setSubject(subject);
            //   message.setText(body);
            //   Transport.send(message);

            System.out.println("[EMAIL] Sending to " + recipient + " via " + config.getHost()
                    + ":" + config.getPort() + " from " + config.getFromAddress());
            System.out.println("[EMAIL] Subject: " + subject);
            System.out.println("[EMAIL] Body: " + body);
        } catch (Exception e) {
            throw new NotificationException(getChannelName(),
                    "Failed to send email to " + recipient + ": " + e.getMessage(), e);
        }
    }
}
