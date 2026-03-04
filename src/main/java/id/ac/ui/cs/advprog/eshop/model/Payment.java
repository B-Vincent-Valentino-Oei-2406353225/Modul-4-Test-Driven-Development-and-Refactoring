package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    
    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        setMethod(method);
        setStatus(status);
        setPaymentData(paymentData);
    }

    public void setMethod(String method) {
        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException();
        } else {
            this.method = method;
        }
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }

    public void setPaymentData(Map<String, String> paymentData) {
        if (paymentData == null) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }
    }
}
