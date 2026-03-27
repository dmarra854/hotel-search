package com.hotel.search.application.service;

import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.domain.model.Search;
import com.hotel.search.domain.model.SearchCount;

public class CountService implements CountUseCase {

    private final SearchRepository searchRepository;

    public CountService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public SearchCount count(String searchId) {
        Search search = searchRepository.findBySearchId(searchId);
        if (search == null) {
            throw new IllegalArgumentException(
                    String.format("No search found for searchId: %s", searchId));
        }
        long count = searchRepository.countBySearchId(searchId);
        return new SearchCount(searchId, search, count);
    }
}
