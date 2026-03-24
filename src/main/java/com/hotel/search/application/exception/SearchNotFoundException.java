package com.hotel.search.application.exception;

public class SearchNotFoundException extends RuntimeException {

    public SearchNotFoundException(String searchId) {
        super(String.format("No search found with identifier: %s", searchId));
    }
}