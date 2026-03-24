package com.hotel.search.infrastructure.adapter.in.rest.validation;

import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckInBeforeCheckOutValidator
        implements ConstraintValidator<CheckInBeforeCheckOut, SearchRequestDTO> {

    @Override
    public boolean isValid(SearchRequestDTO dto, ConstraintValidatorContext context) {
    	boolean isValid = dto.checkIn().isBefore(dto.checkOut());
    
    	if (!isValid) {
        	context.disableDefaultConstraintViolation();
        	context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
               .addPropertyNode("checkIn")
               .addConstraintViolation();
    	}
    
    	return isValid;
	}
}
