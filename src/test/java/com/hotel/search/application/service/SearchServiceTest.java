package com.hotel.search.application.service;

import com.hotel.search.TestFixtures;
import com.hotel.search.application.port.out.SearchEventPublisher;
import com.hotel.search.domain.exception.SearchDomainException;
import com.hotel.search.domain.model.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchService unit tests")
class SearchServiceTest {

    @Mock
    private SearchEventPublisher searchEventPublisher;

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService(searchEventPublisher);
    }

    @Test
    @DisplayName("should generate a non-null UUID searchId without accessing the database")
    void shouldGenerateUuidSearchId() {
        String searchId = searchService.search(TestFixtures.SEARCH_WITHOUT_ID);

        assertAll(
                () -> assertThat(searchId).isNotNull(),
                () -> assertThat(searchId).isNotBlank(),
                () -> assertThat(searchId).matches(
                        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
        );
    }

    @Test
    @DisplayName("should publish an enriched search event with the generated searchId")
    void shouldPublishEnrichedSearchEvent() {
        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);

        String searchId = searchService.search(TestFixtures.SEARCH_WITHOUT_ID);

        verify(searchEventPublisher, times(1)).publish(captor.capture());
        Search published = captor.getValue();

        assertAll(
                () -> assertThat(published.searchId()).isEqualTo(searchId),
                () -> assertThat(published.hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(published.checkIn()).isEqualTo(TestFixtures.CHECK_IN),
                () -> assertThat(published.checkOut()).isEqualTo(TestFixtures.CHECK_OUT),
                () -> assertThat(published.ages()).isEqualTo(TestFixtures.AGES)
        );
    }

    @Test
    @DisplayName("should return distinct IDs for two consecutive searches")
    void shouldReturnDistinctIds() {
        String id1 = searchService.search(TestFixtures.SEARCH_WITHOUT_ID);
        String id2 = searchService.search(TestFixtures.SEARCH_WITHOUT_ID);

        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ages are negative")
    void shouldNotAllowNegativeAges() {
        List<Integer> invalidAges = List.of(30, -1, 5);

        assertThatThrownBy(() -> new Search(
                UUID.randomUUID().toString(),
                "HOTEL_123",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                invalidAges))
                .isInstanceOf(SearchDomainException.class)
                .hasMessageContaining("Domain Error: Invalid ages detected");
    }
}
