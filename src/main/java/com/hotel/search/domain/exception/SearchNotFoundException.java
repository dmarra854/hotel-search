package com.hotel.search.domain.exception;

public class SearchNotFoundException extends RuntimeException {

    public SearchNotFoundException(String searchId) {
        super(String.format("No search found for searchId: %s", searchId));
    }
}
