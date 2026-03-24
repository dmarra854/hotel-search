package com.hotel.search.infrastructure.adapter.out.persistence;

import com.hotel.search.TestFixtures;
import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import com.hotel.search.infrastructure.adapter.out.persistence.mapper.SearchPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("SearchPersistenceMapper unit tests")
class SearchPersistenceMapperTest {

    private SearchPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SearchPersistenceMapper();
    }

    @Test
    @DisplayName("toEntity should map all fields correctly including ages as CSV")
    void shouldMapToEntity() {
        SearchEntity entity = mapper.toEntity(TestFixtures.SEARCH);

        assertAll(
                () -> assertThat(entity.getSearchId()).isEqualTo(TestFixtures.SEARCH_ID),
                () -> assertThat(entity.getHotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(entity.getCheckIn()).isEqualTo(TestFixtures.CHECK_IN),
                () -> assertThat(entity.getCheckOut()).isEqualTo(TestFixtures.CHECK_OUT),
                () -> assertThat(entity.getAges()).isEqualTo("30,29,1,3")
        );
    }

    @Test
    @DisplayName("toDomain should map all fields correctly and preserve age order")
    void shouldMapToDomain() {
        SearchEntity entity = mapper.toEntity(TestFixtures.SEARCH);
        Search domain = mapper.toDomain(entity);

        assertAll(
                () -> assertThat(domain.searchId()).isEqualTo(TestFixtures.SEARCH_ID),
                () -> assertThat(domain.hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(domain.checkIn()).isEqualTo(TestFixtures.CHECK_IN),
                () -> assertThat(domain.checkOut()).isEqualTo(TestFixtures.CHECK_OUT),
                () -> assertThat(domain.ages()).containsExactly(30, 29, 1, 3)
        );
    }

    @Test
    @DisplayName("round-trip toEntity -> toDomain should preserve all values")
    void roundTripShouldPreserveValues() {
        SearchEntity entity = mapper.toEntity(TestFixtures.SEARCH);
        Search restored = mapper.toDomain(entity);

        assertThat(restored).isEqualTo(TestFixtures.SEARCH);
    }
}
