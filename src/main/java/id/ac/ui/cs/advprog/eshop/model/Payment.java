package id.ac.ui.cs.advprog.eshop.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        this.method = method;

        String[] validMethod = {"INITIAL"};
        if (Arrays.asList(validMethod).stream().noneMatch(e->(e.equals(method)))) {
            throw new IllegalArgumentException();
        } else {
            this.method = method;
        }

        String[] validStatus = {"PENDING", "REJECTED", "SUCCESS"};
        if (Arrays.asList(validStatus).stream().noneMatch(e->(e.equals(status)))) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
        
        Map<String, List<String>> validPaymentData = Map.of(
            "INITIAL", List.of("cardNumber")
        );
        if (paymentData == null || 
            paymentData.isEmpty() || 
            validPaymentData.get(method).stream().anyMatch(e->(!paymentData.containsKey(e)))
        ) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }
    }

    public void setStatus(String status) {
        String[] validStatus = {"PENDING", "REJECTED", "SUCCESS"};
        if (Arrays.asList(validStatus).stream().noneMatch(e->(e.equals(status)))) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }
}
