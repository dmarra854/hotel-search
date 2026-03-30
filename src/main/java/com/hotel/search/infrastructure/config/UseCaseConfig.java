package com.hotel.search.infrastructure.config;

import com.hotel.search.application.port.out.SearchEventPublisher;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.application.service.CountService;
import com.hotel.search.application.service.SearchService;
import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.in.SearchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SearchUseCase searchUseCase(SearchEventPublisher searchEventPublisher) {
        return new SearchService(searchEventPublisher);
    }

    @Bean
    public CountUseCase countUseCase(SearchRepository searchRepository) {
        return new CountService(searchRepository);
    }
}
