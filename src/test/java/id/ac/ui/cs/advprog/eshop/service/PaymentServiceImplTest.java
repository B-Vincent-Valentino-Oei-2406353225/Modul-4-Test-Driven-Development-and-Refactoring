package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

	@InjectMocks
	PaymentServiceImpl paymentService;

	@Mock
	PaymentRepository paymentRepository;

	List<Payment> payments;

	@BeforeEach
	void setUp() {
		List<Product> products = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
		product1.setProductName("Sampo Cap Bambang");
		product1.setProductQuantity(2);
		products.add(product1);

		List<Order> orders = new ArrayList<>();
		Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
				products, 1708560000L, "Safira Sudrajat");
		orders.add(order1);
		Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
				products, 1708570000L, "Safira Sudrajat");
		orders.add(order2);

        payments = new ArrayList<>();
        payments.add(new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", PaymentMethod.INITIAL.getValue(), PaymentStatus.PENDING.getValue(), Map.of("cardNumber", "1234567890123456")));
        payments.add(new Payment("a2c62328-4a37-4646-83c7-f32db8620155", PaymentMethod.INITIAL.getValue(), PaymentStatus.REJECTED.getValue(), Map.of("cardNumber", "1234567890123456")));
        payments.add(new Payment("c3f9e15b-4b15-42f4-aebc-c3af385fb078", PaymentMethod.INITIAL.getValue(), PaymentStatus.SUCCESS.getValue(), Map.of("cardNumber", "1234567890123456")));
    }

	@Test
	void testAddPayment() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));
	}

	@Test
	void testAddPaymentIfAlreadyExist() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.addPayment(null, PaymentMethod.INITIAL.getValue(), Map.of("cardNumber", "1234567890123456")));

        verify(paymentRepository, times(0)).save(any(Payment.class));
	}

	@Test
	void testUpdateStatus() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
	}

	@Test
	void testUpdateStatusInvalidStatus() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.setStatus(payment, "INVALID_STATUS"));

        verify(paymentRepository, times(0)).save(any(Payment.class));
	}

	@Test
	void testUpdateStatusInvalidPayment() {
        doReturn(null).when(paymentRepository).findById("non-existent-id");

        assertThrows(NoSuchElementException.class,
                () -> paymentService.setStatus(new Payment("non-existent-id", PaymentMethod.INITIAL.getValue(), PaymentStatus.PENDING.getValue(), Map.of("cardNumber", "1234567890123456")), PaymentStatus.SUCCESS.getValue()));

        verify(paymentRepository, times(0)).save(any(Payment.class));
	}

	@Test
	void testFindByIdIfIdFound() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
	}

	@Test
	void testFindByIdIfIdNotFound() {
        doReturn(null).when(paymentRepository).findById("non-existent-id");

        Payment result = paymentService.getPayment("non-existent-id");
        assertNull(result);
	}

	@Test
	void testFindAll() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> paymentList = paymentService.getAllPayments();
        assertEquals(3, paymentList.size());
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(0).getId())));
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(1).getId())));
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(2).getId())));
	}
}
