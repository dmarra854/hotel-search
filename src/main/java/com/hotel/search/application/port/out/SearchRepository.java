package com.hotel.search.application.port.out;

import com.hotel.search.domain.model.Search;
import java.util.Optional;

public interface SearchRepository {
    void save(Search search);
    long countBySearchId(String searchId);
    Optional<Search> findBySearchId(String searchId);
}