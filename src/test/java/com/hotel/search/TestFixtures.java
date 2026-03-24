package com.hotel.search;

import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Shared, immutable test fixtures used across all test classes.
 * <p>
 * Centralising test data avoids duplication and ensures every test works with
 * the same well-known values. All instances are constructed once and reused.
 * </p>
 */
public final class TestFixtures {

    public static final String SEARCH_ID = "test-search-id-001";
    public static final String HOTEL_ID = "1234aBc";
    public static final LocalDate CHECK_IN = LocalDate.of(2023, 12, 29);
    public static final LocalDate CHECK_OUT = LocalDate.of(2023, 12, 31);
    public static final List<Integer> AGES = List.of(30, 29, 1, 3);

    /** Canonical domain Search with a known searchId. */
    public static final Search SEARCH = new Search(
            SEARCH_ID, HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);

    /** Domain Search without a searchId (pre-assignment state). */
    public static final Search SEARCH_WITHOUT_ID = new Search(
            null, HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);

    /** Valid REST request DTO. */
    public static final SearchRequestDTO REQUEST_DTO = new SearchRequestDTO(
            HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);

    private TestFixtures() {
        // Utility class — no instantiation
    }
}
