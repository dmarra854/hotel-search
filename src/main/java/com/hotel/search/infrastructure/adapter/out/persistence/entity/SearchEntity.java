package com.hotel.search.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "hotel_search")
public class SearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_id", nullable = false)
    private String searchId;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "ages", nullable = false, columnDefinition = "INTEGER[]")
    private int[] ages;

    public SearchEntity() {
    }

    public int[] getAges() { return ages.clone(); }
}
