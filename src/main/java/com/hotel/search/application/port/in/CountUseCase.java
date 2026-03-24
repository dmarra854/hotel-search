package com.hotel.search.application.port.in;

import com.hotel.search.domain.model.SearchCount;

public interface CountUseCase {

    SearchCount count(String searchId);
}
