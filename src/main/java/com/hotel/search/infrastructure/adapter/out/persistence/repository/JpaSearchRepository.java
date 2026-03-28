package com.hotel.search.infrastructure.adapter.out.persistence.repository;

import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSearchRepository extends JpaRepository<SearchEntity, Long> {

    Optional<SearchEntity> findFirstBySearchId(@Param("searchId") String searchId);

    @Query(value = """
            SELECT COUNT(s.id)
            FROM hotel_search s
            JOIN hotel_search ref ON ref.search_id = :searchId
            WHERE s.hotel_id  = ref.hotel_id
              AND s.check_in  = ref.check_in
              AND s.check_out = ref.check_out
              AND s.ages      = ref.ages
            """, nativeQuery = true)
    long countBySameSearchCriteria(@Param("searchId") String searchId);
}
