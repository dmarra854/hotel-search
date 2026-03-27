package com.hotel.search.infrastructure.config;

import com.hotel.search.application.port.out.SearchEventPublisher;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.application.service.CountService;
import com.hotel.search.application.service.SearchService;
import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.in.SearchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that wires the application use cases.
 * <p>
 * This is the only place where Spring annotations appear for the application layer.
 * {@link SearchService} and {@link CountService} are pure Java classes — they have
 * no framework dependency and can be instantiated and tested without a Spring context.
 * </p>
 * <p>
 * If the framework changes in the future, only this class needs to be modified.
 * </p>
 */
@Configuration
public class UseCaseConfig {

    /**
     * Registers {@link SearchService} as the {@link SearchUseCase} bean.
     *
     * @param searchEventPublisher the output port implementation provided by infrastructure
     * @return the configured use case instance
     */
    @Bean
    public SearchUseCase searchUseCase(SearchEventPublisher searchEventPublisher) {
        return new SearchService(searchEventPublisher);
    }

    /**
     * Registers {@link CountService} as the {@link CountUseCase} bean.
     *
     * @param searchRepository the output port implementation provided by infrastructure
     * @return the configured use case instance
     */
    @Bean
    public CountUseCase countUseCase(SearchRepository searchRepository) {
        return new CountService(searchRepository);
    }
}
