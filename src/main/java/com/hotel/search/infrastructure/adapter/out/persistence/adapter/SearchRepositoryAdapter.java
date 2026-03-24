package com.hotel.search.infrastructure.adapter.out.persistence.adapter;

import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.domain.model.Search;
import com.hotel.search.infrastructure.adapter.out.persistence.entity.SearchEntity;
import com.hotel.search.infrastructure.adapter.out.persistence.mapper.SearchPersistenceMapper;
import com.hotel.search.infrastructure.adapter.out.persistence.repository.JpaSearchRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SearchRepositoryAdapter implements SearchRepository {

    private final JpaSearchRepository jpaSearchRepository;
    private final SearchPersistenceMapper mapper;

    public SearchRepositoryAdapter(JpaSearchRepository jpaSearchRepository,
                                   SearchPersistenceMapper mapper) {
        this.jpaSearchRepository = jpaSearchRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Search search) {
        SearchEntity entity = mapper.toEntity(search);
        jpaSearchRepository.save(entity);
    }
    @Override
    public long countBySearchId(String searchId) {
        return jpaSearchRepository.countBySameSearchCriteria(searchId);
    }

    @Override
    public Optional<Search> findBySearchId(String searchId) {
        return jpaSearchRepository.findFirstBySearchId(searchId)
                .map(mapper::toDomain);
    }}
