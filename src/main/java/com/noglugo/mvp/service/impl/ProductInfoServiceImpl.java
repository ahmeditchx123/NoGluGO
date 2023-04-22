package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.ProductInfo;
import com.noglugo.mvp.repository.ProductInfoRepository;
import com.noglugo.mvp.service.ProductInfoService;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
import com.noglugo.mvp.service.mapper.ProductInfoMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductInfo}.
 */
@Service
@Transactional
public class ProductInfoServiceImpl implements ProductInfoService {

    private final Logger log = LoggerFactory.getLogger(ProductInfoServiceImpl.class);

    private final ProductInfoRepository productInfoRepository;

    private final ProductInfoMapper productInfoMapper;

    public ProductInfoServiceImpl(ProductInfoRepository productInfoRepository, ProductInfoMapper productInfoMapper) {
        this.productInfoRepository = productInfoRepository;
        this.productInfoMapper = productInfoMapper;
    }

    @Override
    public ProductInfoDTO save(ProductInfoDTO productInfoDTO) {
        log.debug("Request to save ProductInfo : {}", productInfoDTO);
        ProductInfo productInfo = productInfoMapper.toEntity(productInfoDTO);
        productInfo = productInfoRepository.save(productInfo);
        return productInfoMapper.toDto(productInfo);
    }

    @Override
    public ProductInfoDTO update(ProductInfoDTO productInfoDTO) {
        log.debug("Request to update ProductInfo : {}", productInfoDTO);
        ProductInfo productInfo = productInfoMapper.toEntity(productInfoDTO);
        productInfo = productInfoRepository.save(productInfo);
        return productInfoMapper.toDto(productInfo);
    }

    @Override
    public Optional<ProductInfoDTO> partialUpdate(ProductInfoDTO productInfoDTO) {
        log.debug("Request to partially update ProductInfo : {}", productInfoDTO);

        return productInfoRepository
            .findById(productInfoDTO.getId())
            .map(existingProductInfo -> {
                productInfoMapper.partialUpdate(existingProductInfo, productInfoDTO);

                return existingProductInfo;
            })
            .map(productInfoRepository::save)
            .map(productInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductInfos");
        return productInfoRepository.findAll(pageable).map(productInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductInfoDTO> findOne(UUID id) {
        log.debug("Request to get ProductInfo : {}", id);
        return productInfoRepository.findById(id).map(productInfoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ProductInfo : {}", id);
        productInfoRepository.deleteById(id);
    }
}
