package com.learning.paymentdataaccess.outputadapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.paymentapplicationservice.output.PaymentRepository;
import com.learning.paymentdataaccess.mapper.PaymentJPAMapper;
import com.learning.paymentdataaccess.repositories.PaymentJPARepository;
import com.learning.paymentdomaincore.entities.Payment;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJPAMapper paymentJPAMapper;

	private final PaymentJPARepository paymentJPARepository;

	public PaymentRepositoryImpl(PaymentJPAMapper paymentJPAMapper, PaymentJPARepository paymentJPARepository) {
		this.paymentJPAMapper = paymentJPAMapper;
		this.paymentJPARepository = paymentJPARepository;
	}

	@Override
	public Payment save(Payment payment) {
		return paymentJPAMapper
				.paymentEntityToPayment(paymentJPARepository.save(paymentJPAMapper.paymentToPaymentEntity(payment)));
	}

	@Override
	public Optional<Payment> findByOrderId(OrderId orderId) {
		return paymentJPARepository.findByOrderId(orderId.getValue())
		              .map(paymentJPAMapper :: paymentEntityToPayment);
	}

}
