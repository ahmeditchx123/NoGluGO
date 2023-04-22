package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.repository.StoreRepository;
import com.noglugo.mvp.service.StoreService;
import com.noglugo.mvp.service.dto.StoreDTO;
import com.noglugo.mvp.service.mapper.StoreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Store}.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public StoreDTO update(StoreDTO storeDTO) {
        log.debug("Request to update Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public Optional<StoreDTO> partialUpdate(StoreDTO storeDTO) {
        log.debug("Request to partially update Store : {}", storeDTO);

        return storeRepository
            .findById(storeDTO.getId())
            .map(existingStore -> {
                storeMapper.partialUpdate(existingStore, storeDTO);

                return existingStore;
            })
            .map(storeRepository::save)
            .map(storeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        return storeRepository.findAll(pageable).map(storeMapper::toDto);
    }

    /**
     *  Get all the stores where StoreAddress is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StoreDTO> findAllWhereStoreAddressIsNull() {
        log.debug("Request to get all stores where StoreAddress is null");
        return StreamSupport
            .stream(storeRepository.findAll().spliterator(), false)
            .filter(store -> store.getStoreAddress() == null)
            .map(storeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoreDTO> findOne(UUID id) {
        log.debug("Request to get Store : {}", id);
        return storeRepository.findById(id).map(storeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.deleteById(id);
    }

    @Override
    public Optional<StoreDTO> findOneByUserId(Long userId) {
        return storeRepository.findByUserIdEquals(userId).map(storeMapper::toDto);
    }
}
