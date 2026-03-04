package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private List<Order> orders;
    private Map<String,String> paymentData;

    @BeforeEach
    void setUp() {
        this.orders = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");

        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4646-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");

        this.orders.add(new Order("13652556-012a-4c07-b546-54eb1396d79b",
                List.of(product1, product2), 1708560000L, "Safira Sudrajat"));

        paymentData = Map.of(
            "cardNumber", "1234567890123456"
        );
    }

    @Test
    void testCreatePaymentDefault() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), PaymentStatus.PENDING.getValue(), paymentData);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("INITIAL", payment.getMethod());
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
        assertSame(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentNullData() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), PaymentStatus.PENDING.getValue(), null);
        });
    }

    @Test
    void testCreatePaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), "INVALID_STATUS", paymentData);
        });
    }

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "INVALID_METHOD", PaymentStatus.PENDING.getValue(), paymentData);
        });
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), PaymentStatus.PENDING.getValue(), paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), PaymentStatus.SUCCESS.getValue(), paymentData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }
}
