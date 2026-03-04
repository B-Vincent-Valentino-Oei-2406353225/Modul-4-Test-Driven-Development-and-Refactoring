package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
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
        if (paymentRepository.findById(order.getId()) != null) {
            return null;
        }

        String paymentStatus = PaymentStatus.PENDING.getValue();
        if (PaymentMethod.VOUCHER.getValue().equals(method) && !isValidVoucherData(paymentData)) {
            paymentStatus = PaymentStatus.REJECTED.getValue();
        } else if (PaymentMethod.CASH_ON_DELIVERY.getValue().equals(method) && !isValidCashOnDeliveryData(paymentData)) {
            paymentStatus = PaymentStatus.REJECTED.getValue();
        }

        Payment payment = new Payment(order.getId(), method, paymentStatus, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    private boolean isValidVoucherData(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null) {
            return false;
        }

        String regex = "^ESHOP(?=(?:\\D*\\d){8}\\D*$).{11}$";
        return voucherCode.matches(regex);
    }

    private boolean isValidCashOnDeliveryData(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return address != null && !address.isEmpty() && deliveryFee != null && !deliveryFee.isEmpty();
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
