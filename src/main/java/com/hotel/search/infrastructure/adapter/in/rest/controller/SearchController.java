package com.hotel.search.infrastructure.adapter.in.rest.controller;

import com.hotel.search.application.port.in.CountUseCase;
import com.hotel.search.application.port.in.SearchUseCase;
import com.hotel.search.domain.model.Search;
import com.hotel.search.domain.model.SearchCount;
import com.hotel.search.infrastructure.adapter.in.rest.dto.CountResponseDTO;
import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchRequestDTO;
import com.hotel.search.infrastructure.adapter.in.rest.dto.SearchResponseDTO;
import com.hotel.search.infrastructure.adapter.in.rest.mapper.SearchRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "Hotel Search", description = "Endpoints for hotel availability searches")
public class SearchController {

    private final SearchUseCase searchUseCase;
    private final CountUseCase countUseCase;
    private final SearchRestMapper searchRestMapper;

    public SearchController(SearchUseCase searchUseCase,
                            CountUseCase countUseCase,
                            SearchRestMapper searchRestMapper) {
        this.searchUseCase = searchUseCase;
        this.countUseCase = countUseCase;
        this.searchRestMapper = searchRestMapper;
    }

    @Operation(
            summary = "Submit a hotel availability search",
            description = "Validates the payload, generates a unique searchId and dispatches the search to Kafka for persistence"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Search accepted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content)
    })
    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SearchResponseDTO search(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Hotel search criteria",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Sample search",
                                    summary = "Family stay at hotel 1234aBc",
                                    value = """
                                            {
                                              "hotelId": "1234aBc",
                                              "checkIn": "29/12/2023",
                                              "checkOut": "31/12/2023",
                                              "ages": [30, 29, 1, 3]
                                            }"""
                            )
                    )
            )
            @Valid @RequestBody SearchRequestDTO dto) {
        Search search = searchRestMapper.toDomain(dto);
        String searchId = searchUseCase.search(search);
        return new SearchResponseDTO(searchId);
    }

    @Operation(
            summary = "Count identical searches",
            description = "Returns the search details and the number of times the identical search was performed. Age order matters: [30,29] and [29,30] are different searches."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid or missing searchId",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "searchId not found",
                    content = @Content)
    })
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public CountResponseDTO count(
            @Parameter(
                    description = "The searchId returned by POST /search",
                    required = true,
                    example = "a3f1c2d4-5e6f-7890-abcd-ef1234567890"
            )
            @RequestParam @NotBlank(message = "searchId must not be blank") String searchId) {
        SearchCount searchCount = countUseCase.count(searchId);
        return searchRestMapper.toCountResponseDTO(searchCount);
    }
}
