package com.hotel.search.infrastructure.adapter.out.persistence.mapper;

import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchPersistenceMapper {

    private static final String AGES_DELIMITER = ",";

    public SearchEntity toEntity(Search search) {
        String agesStr = search.ages().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(AGES_DELIMITER));

        return new SearchEntity(
                search.searchId(),
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                agesStr
        );
    }

    public Search toDomain(SearchEntity entity) {
        List<Integer> ages = Arrays.stream(entity.getAges().split(AGES_DELIMITER))
                .map(Integer::parseInt)
                .toList();

        return new Search(
                entity.getSearchId(),
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                ages
        );
    }
}
