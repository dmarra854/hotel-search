package com.hotel.search.domain.model;

import com.hotel.search.domain.exception.SearchDomainException;

import java.time.LocalDate;
import java.util.List;

public record Search(
        String searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public Search {
        if (ages == null || ages.stream().anyMatch(age -> age < 0)) {
            throw new SearchDomainException("Domain Error: Invalid ages detected in search criteria.");
        }

        if (checkOut.isBefore(checkIn)) {
            throw new SearchDomainException("Domain Error: Check-out must be after check-in.");
        }
    }
}