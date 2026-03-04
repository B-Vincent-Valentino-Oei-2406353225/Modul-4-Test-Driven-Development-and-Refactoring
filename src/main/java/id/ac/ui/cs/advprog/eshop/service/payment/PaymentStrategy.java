package id.ac.ui.cs.advprog.eshop.service.payment;

public interface PaymentStrategy {
    public boolean supports(String method);
    public String processPayment(String paymentData);
}
