package com.hotel.search.application;

import com.hotel.search.HotelSearchApplication;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.boot.SpringApplication.run;

class HotelSearchApplicationTest {

    @Test
    void main() {
        // Usamos mockStatic para evitar que Spring realmente intente levantar todo el microservicio
        try (MockedStatic<org.springframework.boot.SpringApplication> mockedSpringApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
            mockedSpringApplication.when(() -> run(HotelSearchApplication.class, new String[]{}))
                    .thenReturn(null);

            HotelSearchApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> run(HotelSearchApplication.class, new String[]{}));
        }
    }
}