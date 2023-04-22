package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.repository.RestaurantRepository;
import com.noglugo.mvp.service.RestaurantService;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import com.noglugo.mvp.service.mapper.RestaurantMapper;
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
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    @Override
    public RestaurantDTO update(RestaurantDTO restaurantDTO) {
        log.debug("Request to update Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    @Override
    public Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        log.debug("Request to partially update Restaurant : {}", restaurantDTO);

        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);

                return existingRestaurant;
            })
            .map(restaurantRepository::save)
            .map(restaurantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toDto);
    }

    /**
     *  Get all the restaurants where RestaurantAddress is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantDTO> findAllWhereRestaurantAddressIsNull() {
        log.debug("Request to get all restaurants where RestaurantAddress is null");
        return StreamSupport
            .stream(restaurantRepository.findAll().spliterator(), false)
            .filter(restaurant -> restaurant.getRestaurantAddress() == null)
            .map(restaurantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the restaurants where RestaurantMenu is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantDTO> findAllWhereRestaurantMenuIsNull() {
        log.debug("Request to get all restaurants where RestaurantMenu is null");
        return StreamSupport
            .stream(restaurantRepository.findAll().spliterator(), false)
            .filter(restaurant -> restaurant.getRestaurantMenu() == null)
            .map(restaurantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantDTO> findOne(UUID id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id).map(restaurantMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
    }
}
