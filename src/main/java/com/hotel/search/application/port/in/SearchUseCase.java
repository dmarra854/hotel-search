package com.hotel.search.application.port.in;

import com.hotel.search.domain.model.Search;

public interface SearchUseCase {

    String search(Search search);
}
