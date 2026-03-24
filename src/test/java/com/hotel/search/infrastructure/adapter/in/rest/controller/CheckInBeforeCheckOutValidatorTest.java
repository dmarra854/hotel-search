package com.hotel.search.infrastructure.adapter.in.rest.controller;

import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;
import com.hotel.search.infrastructure.adapter.in.rest.validation.CheckInBeforeCheckOutValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CheckInBeforeCheckOutValidator unit tests")
class CheckInBeforeCheckOutValidatorTest {

    private CheckInBeforeCheckOutValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CheckInBeforeCheckOutValidator();
    }

    @Test
    @DisplayName("should return true when checkIn is before checkOut")
    void shouldReturnTrueWhenCheckInBeforeCheckOut() {
        SearchRequestDTO dto = new SearchRequestDTO(
                "hotel1",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        );
        assertThat(validator.isValid(dto, null)).isTrue();
    }

    @Test
    @DisplayName("should return false when checkIn is after checkOut")
    void shouldReturnFalseWhenCheckInAfterCheckOut() {
        SearchRequestDTO dto = new SearchRequestDTO(
                "hotel1",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2023, 12, 29),
                List.of(30)
        );
        assertThat(validator.isValid(dto, null)).isFalse();
    }

    @Test
    @DisplayName("should return false when checkIn equals checkOut")
    void shouldReturnFalseWhenCheckInEqualsCheckOut() {
        LocalDate same = LocalDate.of(2023, 12, 29);
        SearchRequestDTO dto = new SearchRequestDTO("hotel1", same, same, List.of(30));
        assertThat(validator.isValid(dto, null)).isFalse();
    }

    @Test
    @DisplayName("should return true when either date is null (delegated to @NotNull)")
    void shouldReturnTrueWhenDatesAreNull() {
        SearchRequestDTO dtoNullCheckIn = new SearchRequestDTO("hotel1", null,
                LocalDate.of(2023, 12, 31), List.of(30));
        SearchRequestDTO dtoNullCheckOut = new SearchRequestDTO("hotel1",
                LocalDate.of(2023, 12, 29), null, List.of(30));

        assertAll(
                () -> assertThat(validator.isValid(dtoNullCheckIn, null)).isTrue(),
                () -> assertThat(validator.isValid(dtoNullCheckOut, null)).isTrue()
        );
    }
}
