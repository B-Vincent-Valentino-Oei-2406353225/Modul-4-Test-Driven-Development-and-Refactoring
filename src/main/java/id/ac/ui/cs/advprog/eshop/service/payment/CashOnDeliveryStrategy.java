package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Component
public class CashOnDeliveryStrategy implements PaymentStrategy {
    @Override
    public boolean supports(String method) {
        return "CASH_ON_DELIVERY".equals(method);
    }

    @Override
    public String processPayment(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        if (address == null || address.isEmpty() || deliveryFee == null || deliveryFee.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        } else {
            return PaymentStatus.PENDING.getValue();
        }
    }
}
