package com.hotel.search.application.service;

import com.hotel.search.TestFixtures;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.domain.model.SearchCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("CountService unit tests")
class CountServiceTest {

    @Mock
    private SearchRepository searchRepository;

    private CountService countService;

    @BeforeEach
    void setUp() {
        countService = new CountService(searchRepository);
    }

    @Test
    @DisplayName("should return SearchCount with correct fields when search exists")
    void shouldReturnSearchCountWhenSearchExists() {
        given(searchRepository.findBySearchId(TestFixtures.SEARCH_ID))
                .willReturn(TestFixtures.SEARCH);
        given(searchRepository.countBySearchId(TestFixtures.SEARCH_ID))
                .willReturn(5L);

        SearchCount result = countService.count(TestFixtures.SEARCH_ID);

        assertAll(
                () -> assertThat(result.searchId()).isEqualTo(TestFixtures.SEARCH_ID),
                () -> assertThat(result.count()).isEqualTo(5L),
                () -> assertThat(result.search()).isEqualTo(TestFixtures.SEARCH),
                () -> assertThat(result.search().hotelId()).isEqualTo(TestFixtures.HOTEL_ID),
                () -> assertThat(result.search().ages()).isEqualTo(TestFixtures.AGES)
        );
    }

    @Test
    @DisplayName("should throw SearchNotFoundException when searchId is not found")
    void shouldThrowWhenSearchIdNotFound() {
        given(searchRepository.findBySearchId("unknown-id")).willReturn(null);
        assertThatThrownBy(() -> countService.count("unknown-id"))
                // Update the expected class here:
                .isInstanceOf(com.hotel.search.domain.exception.SearchNotFoundException.class)
                .hasMessageContaining("unknown-id");
    }
}
