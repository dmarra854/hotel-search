package com.hotel.search.infrastructure.adapter.in.rest.dto;

public record CountResponseDTO(
        String searchId,
        SearchRequestDTO search,
        long count
) {
}
