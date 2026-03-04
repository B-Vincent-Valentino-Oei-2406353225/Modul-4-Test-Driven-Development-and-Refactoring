package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Component
public class VoucherStrategy implements PaymentStrategy {
    @Override
    public boolean supports(String method) {
        return "VOUCHER".equals(method);
    }

    @Override
    public String processPayment(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        String regex = "^ESHOP(?=(?:\\D*\\d){8}\\D*$).{11}$";

        if (voucherCode == null || !voucherCode.matches(regex)) {
            return PaymentStatus.REJECTED.getValue();
        } else {
            return PaymentStatus.PENDING.getValue();
        }
    }
}
