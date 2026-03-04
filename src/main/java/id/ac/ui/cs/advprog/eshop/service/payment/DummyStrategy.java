package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Component
public class DummyStrategy implements PaymentStrategy {
    @Override
    public boolean supports(String method) {
        return "INITIAL".equals(method);
    }

    @Override
    public String processPayment(Map<String, String> paymentData) {
        return PaymentStatus.PENDING.getValue();
    }
}
