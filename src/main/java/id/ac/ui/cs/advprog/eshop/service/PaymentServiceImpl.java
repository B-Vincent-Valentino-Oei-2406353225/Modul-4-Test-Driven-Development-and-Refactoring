package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        if (order == null || method == null || paymentData == null) {
            throw new IllegalArgumentException();
        }
        if (paymentRepository.findById(order.getId()) == null) {
            Payment payment = new Payment(order.getId(), method, PaymentStatus.PENDING.getValue(), paymentData);
            paymentRepository.save(payment);
            return payment;
        }
        return null;
    }

    public Payment setStatus(Payment payment, String status) {
        Payment oldPayment = paymentRepository.findById(payment.getId());
        if (oldPayment == null) {
            throw new IllegalArgumentException();
        }
        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), status, payment.getPaymentData());
        paymentRepository.save(newPayment);
        return newPayment;
    }
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
