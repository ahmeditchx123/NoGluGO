package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.repository.ProductRepository;
import com.noglugo.mvp.service.ProductService;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.mapper.ProductMapper;
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
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        log.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    /**
     *  Get all the products where Information is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllWhereInformationIsNull() {
        log.debug("Request to get all products where Information is null");
        return StreamSupport
            .stream(productRepository.findAll().spliterator(), false)
            .filter(product -> product.getInformation() == null)
            .map(productMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(UUID id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
