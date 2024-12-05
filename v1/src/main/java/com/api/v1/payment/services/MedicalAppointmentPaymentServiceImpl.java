package com.api.v1.payment.services;

import com.api.v1.cards.CardFinderUtil;
import com.api.v1.medical_appointments.MedicalAppointmentFinderUtil;
import com.api.v1.payment.MedicalAppointmentPaymentService;
import com.api.v1.payment.Payment;
import com.api.v1.payment.PaymentRepository;
import com.api.v1.payment.MedicalAppointmentPaymentDto;
import com.api.v1.payment.PaymentResponseDto;
import com.api.v1.payment.exceptions.NotAllowedPaymentException;
import com.api.v1.payment.PaymentResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class MedicalAppointmentPaymentServiceImpl implements MedicalAppointmentPaymentService {

    private final PaymentRepository paymentRepository;
    private final CardFinderUtil cardFinderUtil;
    private final MedicalAppointmentFinderUtil medicalAppointmentFinderUtil;

    @Override
    public Mono<PaymentResponseDto> payMedicalAppointment(@Valid MedicalAppointmentPaymentDto paymentDto) {
        var cardMono = cardFinderUtil.findByNumber(paymentDto.cardNumber());
        var appointmentMono = medicalAppointmentFinderUtil.find(paymentDto.appointmentOrderNumber());
        return cardMono
                .zipWith(appointmentMono)
                .flatMap(tuple -> {
                    if (tuple.getT2().getCompletedAt() == null) {
                        return Mono.error(NotAllowedPaymentException::new);
                    }
                    Payment payment = Payment.create(tuple.getT1(), tuple.getT2(), paymentDto.price());
                    return paymentRepository.save(payment);
                })
                .flatMap(PaymentResponseMapper::mapToMono);
    }
}
