package com.hotel.search.application.service;

import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.application.exception.SearchNotFoundException;
import com.hotel.search.domain.model.SearchCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountService implements CountUseCase {

    private final SearchRepository searchRepository;

    @Override
    public SearchCount count(String searchId) {
        return searchRepository.findBySearchId(searchId)
                .map(search -> {
                    long count = searchRepository.countBySearchId(searchId);
                    return new SearchCount(searchId, search, count);
                })
                .orElseThrow(() -> new SearchNotFoundException(searchId));
    }
}