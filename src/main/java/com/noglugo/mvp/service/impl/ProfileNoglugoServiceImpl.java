package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.security.AuthoritiesConstants;
import com.noglugo.mvp.service.*;
import com.noglugo.mvp.service.dto.*;
import com.noglugo.mvp.web.rest.errors.EmailAlreadyUsedException;
import java.util.List;
import java.util.Optional;
import javax.security.auth.login.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileNoglugoServiceImpl implements ProfileNoglugoService {

    private final UserService userService;
    private final StoreService storeService;

    private final AddressService addressService;

    private final CartService cartService;

    private final OrderService orderService;

    private final FileUploadService fileUploadService;

    public ProfileNoglugoServiceImpl(
        UserService userService,
        StoreService storeService,
        AddressService addressService,
        CartService cartService,
        OrderService orderService,
        FileUploadService fileUploadService
    ) {
        this.userService = userService;
        this.storeService = storeService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public ProfileDTO profileInformation(String email) {
        AdminUserDTO user = null;
        StoreDTO storeDTO = null;
        AddressDTO userAddress = null;
        CartDTO cartDTO = null;
        List<OrderDTO> orderDTOList = null;
        try {
            user = userService.getOneByEmail(email);
            // get user's address if it exists
            Optional<AddressDTO> addressDTOOptional = addressService.findOneByUserId(user.getId());
            if (addressDTOOptional.isPresent()) {
                userAddress = addressDTOOptional.get();
            }
            if (user.getAuthorities().contains(AuthoritiesConstants.CLIENT)) {
                // get User Cart if it exists
                Optional<CartDTO> cartDTOOptional = cartService.findOneByUserId(user.getId());
                if (cartDTOOptional.isPresent()) {
                    cartDTO = cartDTOOptional.get();
                }
                orderDTOList = orderService.findAllByUserId(user.getId());
            }
            // if user is a vendor
            if (user.getAuthorities().contains(AuthoritiesConstants.VENDOR)) {
                Optional<StoreDTO> storeDTOOptional = storeService.findOneByUserId(user.getId());
                if (storeDTOOptional.isPresent()) {
                    storeDTO = storeDTOOptional.get();
                }
            }
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ProfileDTO(user, storeDTO, userAddress, cartDTO, orderDTOList);
    }

    @Override
    public AddressDTO handleAddress(String email, AddressDTO addressDTO) {
        AdminUserDTO user = null;
        try {
            user = userService.getOneByEmail(email);
            addressDTO.setUserId(user.getId());
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
        return addressService.save(addressDTO);
    }

    @Override
    public Optional<AdminUserDTO> uploadImage(String email, MultipartFile image) {
        AdminUserDTO user = null;
        try {
            user = userService.getOneByEmail(email);
            FileUploadPathDTO fileUploadPathDTO = fileUploadService.uploadFile(image);
            user.setImageUrl(fileUploadPathDTO.getFilePath());
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
        return userService.updateUser(user);
    }

    @Override
    public Optional<AdminUserDTO> updateProfileInformation(AdminUserDTO userDTO) {
        Optional<AdminUserDTO> adminUserDTOOptional = userService.checkEmail(userDTO.getEmail());
        if (adminUserDTOOptional.isPresent() && (!adminUserDTOOptional.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        return userService.updateUser(userDTO);
    }

    @Override
    public void changePassword(PasswordChangeDTO passwordChangeDTO) {
        userService.changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }
}
