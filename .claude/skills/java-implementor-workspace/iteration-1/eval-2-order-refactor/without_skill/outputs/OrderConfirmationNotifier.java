public class OrderConfirmationNotifier {
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public OrderConfirmationNotifier(CustomerRepository customerRepository, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.emailService = emailService;
    }

    /**
     * Sends an order confirmation email to the customer.
     *
     * @param order the completed order
     */
    public void notify(Order order) {
        String email = customerRepository.getEmail(order.getCustomerId());
        String body = String.format("Order confirmed! Total: $%.2f", order.getTotal());
        emailService.sendEmail(email, "Order Confirmation", body);
    }
}
