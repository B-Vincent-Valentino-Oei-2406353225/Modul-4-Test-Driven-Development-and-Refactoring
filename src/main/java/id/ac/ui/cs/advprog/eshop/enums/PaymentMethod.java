package id.ac.ui.cs.advprog.eshop.enums;

import java.util.List;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    INITIAL("INITIAL", List.of()),
    VOUCHER("VOUCHER", List.of());

    private final String value;
    private final List<String> requiredPaymentData;
    PaymentMethod(String value, List<String> requiredPaymentData) {
        this.value = value;
        this.requiredPaymentData = requiredPaymentData;
    }

    public static boolean contains(String param) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.name().equals(param)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getRequiredPaymentData() {
        return this.requiredPaymentData;
    }
}
