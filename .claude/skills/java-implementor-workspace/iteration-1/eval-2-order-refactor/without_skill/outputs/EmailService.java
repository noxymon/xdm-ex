public interface EmailService {

    /**
     * Sends an email to the specified address.
     *
     * @param recipientEmail the recipient's email address
     * @param subject        the email subject
     * @param body           the email body
     */
    void sendEmail(String recipientEmail, String subject, String body);
}
