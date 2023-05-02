package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.ProductDTO;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface StoreManagementService {
    ProductDTO addProductToStore(UUID storeId, ProductDTO productDTO);

    ProductDTO uploadProductImage(UUID productId, MultipartFile productImage);

    ProductDTO editProductInformation(UUID productId, ProductDTO productDTO);
}
