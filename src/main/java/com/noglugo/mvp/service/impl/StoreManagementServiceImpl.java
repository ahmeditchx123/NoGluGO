package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.service.*;
import com.noglugo.mvp.service.dto.FileUploadPathDTO;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
import com.noglugo.mvp.service.dto.StoreDTO;
import com.noglugo.mvp.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.security.RandomUtil;

@Service
public class StoreManagementServiceImpl implements StoreManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreManagementServiceImpl.class);

    private final StoreService storeService;

    private final ProductService productService;

    private final FileUploadService fileUploadService;

    private final ProductInfoService productInfoService;

    public StoreManagementServiceImpl(
        StoreService storeService,
        ProductService productService,
        FileUploadService fileUploadService,
        ProductInfoService productInfoService
    ) {
        this.storeService = storeService;
        this.productService = productService;
        this.fileUploadService = fileUploadService;
        this.productInfoService = productInfoService;
    }

    @Override
    public ProductDTO addProductToStore(UUID storeId, ProductDTO productDTO) {
        Optional<StoreDTO> storeDTOOptional = storeService.findOne(storeId);
        if (storeDTOOptional.isPresent()) {
            ProductInfoDTO productInfoDTO = productDTO.getProductInfoDTO();
            StoreDTO storeDTO = storeDTOOptional.get();
            productDTO.setStore(storeDTO);
            productDTO.setSku(RandomUtil.generateRandomAlphanumericString());
            productDTO = productService.save(productDTO);
            if (productInfoDTO != null) {
                productInfoDTO.setProduct(productDTO);
                ProductInfoDTO productInfoDTO1 = productInfoService.save(productInfoDTO);
                productDTO.setProductInfoDTO(productInfoDTO1);
            }
            return productDTO;
        } else {
            throw new BadRequestAlertException("store-not-found", "Store not found with id" + storeId.toString(), "400");
        }
    }

    @Override
    public ProductDTO uploadProductImage(UUID productId, MultipartFile productImage) {
        Optional<ProductDTO> productDTOOptional = productService.findOne(productId);
        if (productDTOOptional.isPresent()) {
            ProductDTO productDTO = productDTOOptional.get();
            FileUploadPathDTO fileUploadPathDTO = fileUploadService.uploadFile(productImage);
            productDTO.setImgPath(fileUploadPathDTO.getFilePath());
            return productService.save(productDTO);
        } else {
            throw new BadRequestAlertException("product-not-found", "product not found", "400");
        }
    }

    @Override
    public ProductDTO editProductInformation(UUID productId, ProductDTO productDTO) {
        return Optional
            .of(productService.findOne(productId))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(productDTO1 -> {
                ProductInfoDTO productInfoDTO = productDTO.getProductInfoDTO();
                ProductInfoDTO productInfoDTO1 = productDTO1.getProductInfoDTO();
                BeanUtils.copyProperties(productInfoDTO, productInfoDTO1);
                productInfoService.save(productInfoDTO1);
                BeanUtils.copyProperties(productDTO, productDTO1);
                return productService.save(productDTO1);
            })
            .get();
    }
}
