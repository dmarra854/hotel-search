package com.hotel.search.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.search.infrastructure.adapter.in.rest.validation.CheckInBeforeCheckOut;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@CheckInBeforeCheckOut
@Schema(description = "Hotel availability search request")
public record SearchRequestDTO(

        @Schema(
                description = "Alphanumeric hotel identifier",
                example = "1234aBc",
                maxLength = 50
        )
        @NotBlank(message = "hotelId must not be blank")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "hotelId must be alphanumeric")
        String hotelId,

        @Schema(
                description = "Check-in date in dd/MM/yyyy format",
                example = "29/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @NotNull(message = "checkIn must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

        @Schema(
                description = "Check-out date in dd/MM/yyyy format (must be after checkIn)",
                example = "31/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @NotNull(message = "checkOut must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

        @ArraySchema(
                schema = @Schema(
                        description = "Age of each guest",
                        example = "30",
                        minimum = "0",
                        maximum = "120"
                ),
                minItems = 1
        )
        @NotEmpty(message = "ages must contain at least one element")
        @NotNull(message = "ages must not be null")
        List<@Min(value = 0, message = "Age cannot be negative") Integer> ages
) {

    public SearchRequestDTO {
        ages = (ages != null) ? List.copyOf(ages) : List.of();
    }
}
