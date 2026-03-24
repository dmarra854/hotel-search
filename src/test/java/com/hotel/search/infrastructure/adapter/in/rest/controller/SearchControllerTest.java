package com.hotel.search.infrastructure.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.TestFixtures;
import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.in.SearchUseCase;
import com.hotel.search.domain.model.SearchCount;
import com.hotel.search.infrastructure.adapter.in.rest.mapper.SearchRestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
@Import({SearchRestMapper.class, GlobalExceptionHandler.class})
@DisplayName("SearchController integration tests")
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SearchUseCase searchUseCase;

    @MockitoBean
    private CountUseCase countUseCase;

    @Test
    @DisplayName("POST /search with valid payload returns 202 and searchId")
    void postSearch_validPayload_returns202() throws Exception {
        given(searchUseCase.search(any())).willReturn(TestFixtures.SEARCH_ID);

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestFixtures.REQUEST_DTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.searchId").value(TestFixtures.SEARCH_ID));
    }

    @Test
    @DisplayName("POST /search with missing hotelId returns 400")
    void postSearch_missingHotelId_returns400() throws Exception {
        String body = """
                {
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [30, 29, 1, 3]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search with checkIn after checkOut returns 400")
    void postSearch_checkInAfterCheckOut_returns400() throws Exception {
        String body = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "31/12/2023",
                  "checkOut": "29/12/2023",
                  "ages": [30, 29]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search with empty ages returns 400")
    void postSearch_emptyAges_returns400() throws Exception {
        String body = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": []
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search with non-alphanumeric hotelId returns 400")
    void postSearch_nonAlphanumericHotelId_returns400() throws Exception {
        String body = """
                {
                  "hotelId": "hotel<script>",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [30]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /count with valid searchId returns 200 and count details")
    void getCount_validSearchId_returns200() throws Exception {
        SearchCount searchCount = new SearchCount(
                TestFixtures.SEARCH_ID, TestFixtures.SEARCH, 100L);
        given(countUseCase.count(eq(TestFixtures.SEARCH_ID))).willReturn(searchCount);

        mockMvc.perform(get("/count")
                        .param("searchId", TestFixtures.SEARCH_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value(TestFixtures.SEARCH_ID))
                .andExpect(jsonPath("$.count").value(100))
                .andExpect(jsonPath("$.search.hotelId").value(TestFixtures.HOTEL_ID));
    }

    @Test
    @DisplayName("GET /count with unknown searchId returns 404")
    void getCount_unknownSearchId_returns404() throws Exception {
        given(countUseCase.count(eq("unknown-id")))
                .willThrow(new IllegalArgumentException("No search found for searchId: unknown-id"));

        mockMvc.perform(get("/count")
                        .param("searchId", "unknown-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /count with blank searchId returns 400")
    void getCount_blankSearchId_returns400() throws Exception {
        mockMvc.perform(get("/count")
                        .param("searchId", "   "))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /count without searchId param returns 400")
    void getCount_missingSearchId_returns400() throws Exception {
        mockMvc.perform(get("/count"))
                .andExpect(status().isBadRequest());
    }
}
