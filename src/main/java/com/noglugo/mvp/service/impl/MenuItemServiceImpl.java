package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.repository.MenuItemRepository;
import com.noglugo.mvp.service.MenuItemService;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.service.mapper.MenuItemMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuItem}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        log.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public MenuItemDTO update(MenuItemDTO menuItemDTO) {
        log.debug("Request to update MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO) {
        log.debug("Request to partially update MenuItem : {}", menuItemDTO);

        return menuItemRepository
            .findById(menuItemDTO.getId())
            .map(existingMenuItem -> {
                menuItemMapper.partialUpdate(existingMenuItem, menuItemDTO);

                return existingMenuItem;
            })
            .map(menuItemRepository::save)
            .map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll(pageable).map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> findOne(UUID id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id).map(menuItemMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
