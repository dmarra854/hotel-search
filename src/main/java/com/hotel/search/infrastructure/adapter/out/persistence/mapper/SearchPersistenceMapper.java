package com.hotel.search.infrastructure.adapter.out.persistence.mapper;

import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPersistenceMapper {

    public SearchEntity toEntity(Search domain) {
        if (domain == null) return null;

        int[] agesArray = domain.ages().stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return new SearchEntity(
                null,
                domain.searchId(),
                domain.hotelId(),
                domain.checkIn(),
                domain.checkOut(),
                agesArray
        );
    }

    public Search toDomain(SearchEntity entity) {
        if (entity == null) return null;

        List<Integer> agesList = Arrays.stream(entity.getAges())
                .boxed()
                .toList();

        return new Search(
                entity.getSearchId(),
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                agesList
        );
    }}