package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.service.StoreManagementService;
import com.noglugo.mvp.service.dto.ProductDTO;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("api/v1/store-management")
public class StoreManagementResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreManagementResource.class);

    private final StoreManagementService storeManagementService;

    public StoreManagementResource(StoreManagementService storeManagementService) {
        this.storeManagementService = storeManagementService;
    }

    @PostMapping("add-products/{storeId}")
    public ResponseEntity<?> addProductToStore(@PathVariable("storeId") UUID storeId, @RequestBody ProductDTO productDTO) {
        LOGGER.debug("Rest controller to add product {} to store {}", productDTO.getName(), storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeManagementService.addProductToStore(storeId, productDTO));
    }

    @RequestMapping(value = "upload-product-image/{productId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProductImage(@PathVariable("productId") UUID productId, @RequestParam("image") MultipartFile image) {
        LOGGER.debug("Rest to upload product image to the product {} ...", productId.toString());
        return ResponseEntity.ok().body(storeManagementService.uploadProductImage(productId, image));
    }

    @PutMapping("edit-product/{productId}")
    public ResponseEntity<?> editProductInfos(@PathVariable("productId") UUID productId, @RequestBody ProductDTO productDTO) {
        LOGGER.debug("Rest to edit product information {} ...", productId.toString());
        return ResponseEntity.ok().body(storeManagementService.editProductInformation(productId, productDTO));
    }
}
