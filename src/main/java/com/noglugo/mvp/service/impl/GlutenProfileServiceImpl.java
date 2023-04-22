package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.GlutenProfile;
import com.noglugo.mvp.repository.GlutenProfileRepository;
import com.noglugo.mvp.service.GlutenProfileService;
import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import com.noglugo.mvp.service.mapper.GlutenProfileMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GlutenProfile}.
 */
@Service
@Transactional
public class GlutenProfileServiceImpl implements GlutenProfileService {

    private final Logger log = LoggerFactory.getLogger(GlutenProfileServiceImpl.class);

    private final GlutenProfileRepository glutenProfileRepository;

    private final GlutenProfileMapper glutenProfileMapper;

    public GlutenProfileServiceImpl(GlutenProfileRepository glutenProfileRepository, GlutenProfileMapper glutenProfileMapper) {
        this.glutenProfileRepository = glutenProfileRepository;
        this.glutenProfileMapper = glutenProfileMapper;
    }

    @Override
    public GlutenProfileDTO save(GlutenProfileDTO glutenProfileDTO) {
        log.debug("Request to save GlutenProfile : {}", glutenProfileDTO);
        GlutenProfile glutenProfile = glutenProfileMapper.toEntity(glutenProfileDTO);
        glutenProfile = glutenProfileRepository.save(glutenProfile);
        return glutenProfileMapper.toDto(glutenProfile);
    }

    @Override
    public GlutenProfileDTO update(GlutenProfileDTO glutenProfileDTO) {
        log.debug("Request to update GlutenProfile : {}", glutenProfileDTO);
        GlutenProfile glutenProfile = glutenProfileMapper.toEntity(glutenProfileDTO);
        glutenProfile = glutenProfileRepository.save(glutenProfile);
        return glutenProfileMapper.toDto(glutenProfile);
    }

    @Override
    public Optional<GlutenProfileDTO> partialUpdate(GlutenProfileDTO glutenProfileDTO) {
        log.debug("Request to partially update GlutenProfile : {}", glutenProfileDTO);

        return glutenProfileRepository
            .findById(glutenProfileDTO.getId())
            .map(existingGlutenProfile -> {
                glutenProfileMapper.partialUpdate(existingGlutenProfile, glutenProfileDTO);

                return existingGlutenProfile;
            })
            .map(glutenProfileRepository::save)
            .map(glutenProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GlutenProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GlutenProfiles");
        return glutenProfileRepository.findAll(pageable).map(glutenProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GlutenProfileDTO> findOne(UUID id) {
        log.debug("Request to get GlutenProfile : {}", id);
        return glutenProfileRepository.findById(id).map(glutenProfileMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GlutenProfile : {}", id);
        glutenProfileRepository.deleteById(id);
    }
}
