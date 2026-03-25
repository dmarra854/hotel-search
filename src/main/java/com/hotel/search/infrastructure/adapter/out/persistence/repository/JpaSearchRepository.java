package com.hotel.search.infrastructure.adapter.out.persistence.repository;

import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSearchRepository extends JpaRepository<SearchEntity, Long> {

    Optional<SearchEntity> findFirstBySearchId(@Param("searchId") String searchId);

    @Query("""
            SELECT COUNT(s) FROM SearchEntity s
            WHERE s.hotelId   = (SELECT r.hotelId   FROM SearchEntity r WHERE r.searchId = :searchId)
              AND s.checkIn   = (SELECT r.checkIn   FROM SearchEntity r WHERE r.searchId = :searchId)
              AND s.checkOut  = (SELECT r.checkOut  FROM SearchEntity r WHERE r.searchId = :searchId)
              AND s.ages      = (SELECT r.ages      FROM SearchEntity r WHERE r.searchId = :searchId)
            """)
    long countBySameSearchCriteria(@Param("searchId") String searchId);
}
