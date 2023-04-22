package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.repository.MenuRepository;
import com.noglugo.mvp.service.MenuService;
import com.noglugo.mvp.service.dto.MenuDTO;
import com.noglugo.mvp.service.mapper.MenuMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Menu}.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    @Override
    public MenuDTO save(MenuDTO menuDTO) {
        log.debug("Request to save Menu : {}", menuDTO);
        Menu menu = menuMapper.toEntity(menuDTO);
        menu = menuRepository.save(menu);
        return menuMapper.toDto(menu);
    }

    @Override
    public MenuDTO update(MenuDTO menuDTO) {
        log.debug("Request to update Menu : {}", menuDTO);
        Menu menu = menuMapper.toEntity(menuDTO);
        menu = menuRepository.save(menu);
        return menuMapper.toDto(menu);
    }

    @Override
    public Optional<MenuDTO> partialUpdate(MenuDTO menuDTO) {
        log.debug("Request to partially update Menu : {}", menuDTO);

        return menuRepository
            .findById(menuDTO.getId())
            .map(existingMenu -> {
                menuMapper.partialUpdate(existingMenu, menuDTO);

                return existingMenu;
            })
            .map(menuRepository::save)
            .map(menuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menuRepository.findAll(pageable).map(menuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuDTO> findOne(UUID id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findById(id).map(menuMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.deleteById(id);
    }
}
