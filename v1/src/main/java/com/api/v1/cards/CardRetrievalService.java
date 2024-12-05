package com.api.v1.cards;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardRetrievalService {
    Mono<Card> findByNumber(String cardNumber);
    Flux<Card> findAll();
    Flux<Card> findAllCreditCards();
    Flux<Card> findAllDebitCards();
}