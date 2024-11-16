package com.api.v1.people.services;

import com.api.v1.db.DbSets;
import com.api.v1.people.domain.Person;
import com.api.v1.people.dtos.PersonRegistrationDto;
import com.api.v1.people.exceptions.DuplicatedEmailException;
import com.api.v1.people.exceptions.DuplicatedSsnException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class PersonRegistrationServiceImpl implements PersonRegistrationService {

    @Override
    public Mono<String> register(@Valid PersonRegistrationDto registrationDto) {
        return Mono.defer(() -> {
            try {
                boolean isSsnDuplicated = !DbSets
                        .peopleCollection()
                        .whereEqualTo("ssn", registrationDto.ssn())
                        .get()
                        .get()
                        .isEmpty();
                if (isSsnDuplicated) {
                    return Mono.error(DuplicatedSsnException::new);
                }
                boolean isEmailDuplicated = !DbSets
                        .peopleCollection()
                        .whereEqualTo("email", registrationDto.email())
                        .get()
                        .get()
                        .isEmpty();
                if (isEmailDuplicated) {
                    return Mono.error(DuplicatedEmailException::new);
                }
                Person person = Person.create(registrationDto);
                var savedPerson = DbSets.peopleCollection()
                        .add(person);
                String personId = savedPerson
                        .get()
                        .getId();
                return Mono.just(personId);
            } catch (Exception ignored) {
                return Mono.empty();
            }
        });
    }

}
