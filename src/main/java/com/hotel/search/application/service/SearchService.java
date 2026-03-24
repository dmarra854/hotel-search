package com.hotel.search.application.service;

import com.hotel.search.application.port.in.SearchUseCase;
import com.hotel.search.application.port.out.SearchEventPublisher;
import com.hotel.search.domain.model.Search;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class SearchService implements SearchUseCase {

    private final SearchEventPublisher searchEventPublisher;

    public SearchService(SearchEventPublisher searchEventPublisher) {
        this.searchEventPublisher = searchEventPublisher;
    }

    @Override
    public String search(Search search) {
        String searchId = UUID.randomUUID().toString();

        Search enrichedSearch = new Search(
                searchId,
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        searchEventPublisher.publish(enrichedSearch);

        return searchId;
    }
}
