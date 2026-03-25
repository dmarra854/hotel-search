package com.hotel.search.application.port.out;

import com.hotel.search.domain.model.Search;

public interface SearchEventPublisher {
    void publish(Search search);
}
