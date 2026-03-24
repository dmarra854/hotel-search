package com.hotel.search.infrastructure.adapter.in.rest.controller;

import com.hotel.search.TestFixtures;
import com.hotel.search.domain.model.SearchCount;
import com.hotel.search.infrastructure.adapter.in.rest.dto.CountResponseDTO;
import com.hotel.search.infrastructure.adapter.in.rest.mapper.SearchRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("SearchRestMapper unit tests")
class SearchRestMapperTest {

    private SearchRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SearchRestMapper();
    }

    @Test
    @DisplayName("toDomain should map DTO to domain without searchId")
    void shouldMapToDomain() {
        var domain = mapper.toDomain(TestFixtures.REQUEST_DTO);

        assertAll(
                () -> assertThat(domain.searchId()).isNull(),
                () -> assertThat(domain.hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(domain.checkIn()).isEqualTo(TestFixtures.CHECK_IN),
                () -> assertThat(domain.checkOut()).isEqualTo(TestFixtures.CHECK_OUT),
                () -> assertThat(domain.ages()).isEqualTo(TestFixtures.AGES)
        );
    }

    @Test
    @DisplayName("toCountResponseDTO should map SearchCount to CountResponseDTO")
    void shouldMapToCountResponseDTO() {
        SearchCount searchCount = new SearchCount(
                TestFixtures.SEARCH_ID, TestFixtures.SEARCH, 42L);

        CountResponseDTO dto = mapper.toCountResponseDTO(searchCount);

        assertAll(
                () -> assertThat(dto.searchId()).isEqualTo(TestFixtures.SEARCH_ID),
                () -> assertThat(dto.count()).isEqualTo(42L),
                () -> assertThat(dto.search().hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(dto.search().ages()).isEqualTo(TestFixtures.AGES),
                () -> assertThat(dto.search().checkIn()).isEqualTo(TestFixtures.CHECK_IN),
                () -> assertThat(dto.search().checkOut()).isEqualTo(TestFixtures.CHECK_OUT)
        );
    }
}
