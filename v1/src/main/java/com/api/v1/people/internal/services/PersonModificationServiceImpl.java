package com.api.v1.people.internal.services;

import com.api.v1.people.PersonModificationService;
import com.api.v1.people.Person;
import com.api.v1.people.PersonAuditTrail;
import com.api.v1.people.PersonAuditTrailRepository;
import com.api.v1.people.PersonRepository;
import com.api.v1.people.PersonModificationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class PersonModificationServiceImpl implements PersonModificationService {

    private final PersonAuditTrailRepository personAuditTrailRepository;
    private final PersonRepository personRepository;

    public PersonModificationServiceImpl(
            PersonAuditTrailRepository personAuditTrailRepository,
            PersonRepository personRepository
    ) {
        this.personAuditTrailRepository = personAuditTrailRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Mono<Person> modify(@NotNull Person person, @Valid PersonModificationDto modificationDto) {
        return personAuditTrailRepository
                .save(PersonAuditTrail.create(person))
                .then(Mono.defer(() -> {
                    person.modify(modificationDto);
                    return personRepository.save(person);
                }));
    }
}