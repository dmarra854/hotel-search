package com.hotel.search.infrastructure.adapter.out.persistence;

import com.hotel.search.TestFixtures;
import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.out.persistence.adapter.SearchRepositoryAdapter;
import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import com.hotel.search.infrastructure.adapter.out.persistence.mapper.SearchPersistenceMapper;
import com.hotel.search.infrastructure.adapter.out.persistence.repository.JpaSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchRepositoryAdapter unit tests")
class SearchRepositoryAdapterTest {

    @Mock
    private JpaSearchRepository jpaSearchRepository;

    private SearchPersistenceMapper mapper;
    private SearchRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        mapper = new SearchPersistenceMapper();
        adapter = new SearchRepositoryAdapter(jpaSearchRepository, mapper);
    }

    @Test
    @DisplayName("save should convert domain to entity and call JPA save")
    void shouldSaveSearch() {
        adapter.save(TestFixtures.SEARCH);
        verify(jpaSearchRepository).save(any(SearchEntity.class));
    }

    @Test
    @DisplayName("countBySearchId should delegate to JPA repository")
    void shouldCountBySearchId() {
        given(jpaSearchRepository.countBySameSearchCriteria(TestFixtures.SEARCH_ID)).willReturn(7L);

        long count = adapter.countBySearchId(TestFixtures.SEARCH_ID);

        assertThat(count).isEqualTo(7L);
    }

    @Test
    @DisplayName("findBySearchId should return domain model when entity exists")
    void shouldReturnSearchWhenFound() {
        SearchEntity entity = mapper.toEntity(TestFixtures.SEARCH);
        given(jpaSearchRepository.findFirstBySearchId(TestFixtures.SEARCH_ID))
                .willReturn(Optional.of(entity));

        Search result = adapter.findBySearchId("some-id").orElseThrow();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.searchId()).isEqualTo(TestFixtures.SEARCH_ID),
                () -> assertThat(result.hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(result.ages()).isEqualTo(TestFixtures.AGES)
        );
    }

    @Test
    @DisplayName("findBySearchId should return null when entity not found")
    void shouldReturnNullWhenNotFound() {
        given(jpaSearchRepository.findFirstBySearchId("missing-id"))
                .willReturn(Optional.empty());

        Search result = adapter.findBySearchId("missing-id").orElseThrow();

        assertThat(result).isNull();
    }
}
