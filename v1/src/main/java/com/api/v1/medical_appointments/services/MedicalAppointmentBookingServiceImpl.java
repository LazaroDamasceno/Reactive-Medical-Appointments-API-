package com.api.v1.medical_appointments.services;

import com.api.v1.customers.domain.Customer;
import com.api.v1.customers.utils.CustomerFinderUtil;
import com.api.v1.doctors.domain.Doctor;
import com.api.v1.doctors.utils.DoctorFinderUtil;
import com.api.v1.medical_appointments.domain.MedicalAppointment;
import com.api.v1.medical_appointments.domain.MedicalAppointmentRepository;
import com.api.v1.medical_appointments.dtos.MedicalAppointmentBookingDto;
import com.api.v1.medical_appointments.dtos.MedicalAppointmentResponseDto;
import com.api.v1.medical_appointments.exceptions.UnavailableSlotException;
import com.api.v1.medical_appointments.utils.MedicalAppointmentResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class MedicalAppointmentBookingServiceImpl implements MedicalAppointmentBookingService {

    private final CustomerFinderUtil customerFinderUtil;
    private final DoctorFinderUtil doctorFinderUtil;
    private final MedicalAppointmentRepository medicalAppointmentRepository;

    @Override
    public Mono<MedicalAppointmentResponseDto> bookPaidMedicalAppointment(@Valid MedicalAppointmentBookingDto bookingDto) {
        Mono<Customer> customerMono = customerFinderUtil.find(bookingDto.ssn());
        Mono<Doctor> doctorMono = doctorFinderUtil.find(bookingDto.medicalLicenseNumber());
        return customerMono
                .zipWith(doctorMono)
                .flatMap(tuple -> {
                    return medicalAppointmentRepository
                            .find(tuple.getT1(), tuple.getT2(), bookingDto.bookingDate().toString())
                            .singleOptional()
                            .flatMap(optional -> {
                               if (optional.isPresent()) {
                                   return Mono.error(new UnavailableSlotException(bookingDto.bookingDate(), bookingDto.medicalLicenseNumber()));
                               }
                               return Mono.defer(() -> {
                                  MedicalAppointment medicalAppointment = MedicalAppointment.create(
                                          tuple.getT1(),
                                          tuple.getT2(),
                                          bookingDto.bookingDate(),
                                          "Medical appointment covered by the customer"
                                  );
                                  return medicalAppointmentRepository.save(medicalAppointment);
                               });
                            });
                })
                .flatMap(MedicalAppointmentResponseMapper::mapToMono);
    }

    @Override
    public Mono<MedicalAppointmentResponseDto> bookAffordableMedicalAppointment(@Valid MedicalAppointmentBookingDto bookingDto) {
        Mono<Customer> customerMono = customerFinderUtil.find(bookingDto.ssn());
        Mono<Doctor> doctorMono = doctorFinderUtil.find(bookingDto.medicalLicenseNumber());
        return customerMono
                .zipWith(doctorMono)
                .flatMap(tuple -> medicalAppointmentRepository
                        .find(tuple.getT1(), tuple.getT2(), bookingDto.bookingDate().toString())
                        .singleOptional()
                        .flatMap(optional -> {
                            if (optional.isPresent()) {
                                return Mono.error(new UnavailableSlotException(bookingDto.bookingDate(), bookingDto.medicalLicenseNumber()));
                            }
                            return Mono.defer(() -> {
                                MedicalAppointment medicalAppointment = MedicalAppointment.create(
                                        tuple.getT1(),
                                        tuple.getT2(),
                                        bookingDto.bookingDate(),
                                        "Medical appointment covered by the Affordable Care Act"
                                );
                                return medicalAppointmentRepository.save(medicalAppointment);
                            });
                }))
                .flatMap(MedicalAppointmentResponseMapper::mapToMono);
    }

    @Override
    public Mono<MedicalAppointmentResponseDto> bookPrivateHeathCareMedicalAppointment(MedicalAppointmentBookingDto bookingDto) {
        Mono<Customer> customerMono = customerFinderUtil.find(bookingDto.ssn());
        Mono<Doctor> doctorMono = doctorFinderUtil.find(bookingDto.medicalLicenseNumber());
        return customerMono
                .zipWith(doctorMono)
                .flatMap(tuple -> {
                    return medicalAppointmentRepository
                            .find(tuple.getT1(), tuple.getT2(), bookingDto.bookingDate().toString())
                            .singleOptional()
                            .flatMap(optional -> {
                                if (optional.isPresent()) {
                                    return Mono.error(new UnavailableSlotException(bookingDto.bookingDate(), bookingDto.medicalLicenseNumber()));
                                }
                                return Mono.defer(() -> {
                                    MedicalAppointment medicalAppointment = MedicalAppointment.create(
                                            tuple.getT1(),
                                            tuple.getT2(),
                                            bookingDto.bookingDate(),
                                            "Medical appointment covered by the private health care"
                                    );
                                    return medicalAppointmentRepository.save(medicalAppointment);
                                });
                            });
                })
                .flatMap(MedicalAppointmentResponseMapper::mapToMono);
    }
}