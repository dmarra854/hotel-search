package com.hotel.search.infrastructure.adapter.in.rest.mapper;

import com.hotel.search.domain.model.Search;
import com.hotel.search.domain.model.SearchCount;
import com.hotel.search.infrastructure.adapter.in.rest.dto.CountResponseDTO;
import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class SearchRestMapper {

    public Search toDomain(SearchRequestDTO dto) {
        return new Search(
                null,
                dto.hotelId(),
                dto.checkIn(),
                dto.checkOut(),
                dto.ages()
        );
    }

    public CountResponseDTO toCountResponseDTO(SearchCount searchCount) {
        SearchRequestDTO searchDTO = new SearchRequestDTO(
                searchCount.search().hotelId(),
                searchCount.search().checkIn(),
                searchCount.search().checkOut(),
                searchCount.search().ages()
        );
        return new CountResponseDTO(
                searchCount.searchId(),
                searchDTO,
                searchCount.count()
        );
    }
}
