package com.api.v1.customers.services;

import com.api.v1.customers.CustomerModificationService;
import com.api.v1.customers.CustomerRepository;
import com.api.v1.customers.CustomerFinderUtil;
import com.api.v1.people.SSN;
import com.api.v1.people.PersonModificationDto;
import com.api.v1.people.PersonModificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerModificationServiceImpl implements CustomerModificationService {

    private final CustomerFinderUtil customerFinderUtil;
    private final PersonModificationService personModificationService;
    private final CustomerRepository customerRepository;

    @Override
    public Mono<Void> modify(@SSN String ssn, @Valid PersonModificationDto modificationDto) {
        return customerFinderUtil
                .find(ssn)
                .flatMap(customer -> personModificationService
                        .modify(customer.getPerson(), modificationDto)
                        .flatMap(modifiedPerson -> {
                            customer.setPerson(modifiedPerson);
                            return customerRepository.save(customer);
                        })
                ).then();
    }

}
