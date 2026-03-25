package com.hotel.search.application.port.out;

import com.hotel.search.domain.model.Search;

public interface SearchRepository {

    void save(Search search);
    long countBySearchId(String searchId);
    Search findBySearchId(String searchId);
}
