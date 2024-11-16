package com.api.v1.doctors.domain;

import com.api.v1.doctors.dtos.DoctorLicenseNumberDto;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DoctorRepository extends ReactiveMongoRepository<Doctor, ObjectId> {

    @Query("{ 'licenseNumber': ?0 }")
    Mono<Doctor> findByLicenseNumber(DoctorLicenseNumberDto licenseNumber);

}
