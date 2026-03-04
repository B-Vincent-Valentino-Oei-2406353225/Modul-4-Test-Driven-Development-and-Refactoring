package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

public interface PaymentStrategy {
    public boolean supports(String method);
    public String processPayment(Map<String, String> paymentData);
}
