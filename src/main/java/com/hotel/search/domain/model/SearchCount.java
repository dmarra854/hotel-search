package com.hotel.search.domain.model;

public record SearchCount(
        String searchId,
        Search search,
        long count
) {
}
