package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.CashOnDeliveryStrategy;
import id.ac.ui.cs.advprog.eshop.service.payment.DummyStrategy;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentStrategy;
import id.ac.ui.cs.advprog.eshop.service.payment.VoucherStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

	PaymentServiceImpl paymentService;

	@Mock
	PaymentRepository paymentRepository;

	List<Payment> payments;
	List<Order> orders;

	@BeforeEach
	void setUp() {
		List<PaymentStrategy> paymentStrategies = List.of(
				new DummyStrategy(),
				new VoucherStrategy(),
				new CashOnDeliveryStrategy()
		);
		paymentService = new PaymentServiceImpl(paymentRepository, paymentStrategies);

		List<Product> products = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
		product1.setProductName("Sampo Cap Bambang");
		product1.setProductQuantity(2);
		products.add(product1);

		orders = new ArrayList<>();
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
		Order order = orders.get(1);
		doReturn(null).when(paymentRepository).findById(order.getId());

		Payment result = paymentService.addPayment(
				order,
				PaymentMethod.INITIAL.getValue(),
				Map.of("cardNumber", "1234567890123456")
		);

		assertNotNull(result);
		assertEquals(order.getId(), result.getId());
		assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void testAddPaymentIfAlreadyExist() {
		Order order = orders.get(1);
		Payment existingPayment = payments.get(1);
		doReturn(existingPayment).when(paymentRepository).findById(order.getId());

		Payment result = paymentService.addPayment(
				order,
				PaymentMethod.INITIAL.getValue(),
				Map.of("cardNumber", "1234567890123456")
		);

		assertNull(result);
		verify(paymentRepository, times(0)).save(any(Payment.class));
	}

    @Test
    void testAddPaymentVoucher() {
        Order order = orders.get(1);
        doReturn(null).when(paymentRepository).findById(order.getId());

        Payment result = paymentService.addPayment(
                order,
                PaymentMethod.VOUCHER.getValue(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidDataKey() {
        Order order = orders.get(1);
        doReturn(null).when(paymentRepository).findById(order.getId());

        Payment result = paymentService.addPayment(
            order,
            PaymentMethod.VOUCHER.getValue(),
            Map.of("invalidKey", "ESHOP1234ABC5678")
        );

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalidDataValue() {
        Order order = orders.get(1);
        doReturn(null).when(paymentRepository).findById(order.getId());

        Payment result = paymentService.addPayment(
            order,
            PaymentMethod.VOUCHER.getValue(),
            Map.of("voucherCode", "INVALID_VOUCHER")
        );

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

	@Test
	void testAddPaymentCashOnDelivery() {
		Order order = orders.get(1);
		doReturn(null).when(paymentRepository).findById(order.getId());

		Payment result = paymentService.addPayment(
				order,
				PaymentMethod.CASH_ON_DELIVERY.getValue(),
				Map.of("address", "Jl. Merdeka No. 123", 
				"deliveryFee", "10000")
		);

		assertNotNull(result);
		assertEquals(order.getId(), result.getId());
		assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void testAddPaymentCashOnDeliveryInvalidData() {
		Order order = orders.get(1);
		doReturn(null).when(paymentRepository).findById(order.getId());

		Payment result = paymentService.addPayment(
				order,
				PaymentMethod.CASH_ON_DELIVERY.getValue(),
				Map.of("invalidKey", "invalidValue")
		);

		assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
	}

	@Test
	void testAddPaymentCashOnDeliveryPartiallInvalidData() {
		Order order = orders.get(1);
		doReturn(null).when(paymentRepository).findById(order.getId());

		Payment result = paymentService.addPayment(
				order,
				PaymentMethod.CASH_ON_DELIVERY.getValue(),
				Map.of("address", "Jl. Merdeka No. 123")
		);

		assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
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

	assertThrows(IllegalArgumentException.class,
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
	doReturn(payments).when(paymentRepository).findAll();

        List<Payment> paymentList = paymentService.getAllPayments();
        assertEquals(3, paymentList.size());
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(0).getId())));
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(1).getId())));
        assertTrue(paymentList.stream().anyMatch(e -> e.getId().equals(payments.get(2).getId())));
	}
}
