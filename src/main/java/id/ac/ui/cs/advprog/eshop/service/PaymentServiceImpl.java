package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentStrategy;

@Service
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;
    private List<PaymentStrategy> paymentStrategies;

    public PaymentServiceImpl(PaymentRepository paymentRepository, List<PaymentStrategy> paymentStrategies) {
        this.paymentRepository = paymentRepository;
        this.paymentStrategies = paymentStrategies;
    }

    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        if (order == null || method == null || paymentData == null) {
            throw new IllegalArgumentException();
        }

        if (paymentRepository.findById(order.getId()) != null) {
            return null;
        }

        String paymentStatus = paymentStrategies.stream()
            .filter(strategy -> strategy.supports(method))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .processPayment(paymentData);

        Payment payment = new Payment(order.getId(), method, paymentStatus, paymentData);
        paymentRepository.save(payment);
        return payment;
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
