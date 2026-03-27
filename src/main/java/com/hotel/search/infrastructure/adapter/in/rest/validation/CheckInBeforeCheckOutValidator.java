package com.hotel.search.infrastructure.adapter.in.rest.validation;

import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CheckInBeforeCheckOutValidator
        implements ConstraintValidator<CheckInBeforeCheckOut, SearchRequestDTO> {

    @Override
    public boolean isValid(SearchRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.checkIn() == null || dto.checkOut() == null) {
            return true; // @NotNull handles nulls separately
        }
        return dto.checkIn().isBefore(dto.checkOut());
    }
}
