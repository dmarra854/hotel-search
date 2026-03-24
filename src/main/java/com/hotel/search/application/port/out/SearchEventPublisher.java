package com.hotel.search.application.port.out;

import com.hotel.search.domain.model.Search;
import jakarta.validation.constraints.NotNull;

public interface SearchEventPublisher {

    void publish(@NotNull Search search);
}