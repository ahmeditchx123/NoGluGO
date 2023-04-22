package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.dto.AdminUserDTO;
import com.noglugo.mvp.service.dto.PasswordChangeDTO;
import com.noglugo.mvp.service.dto.ProfileDTO;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileNoglugoService {
    ProfileDTO profileInformation(String email);

    AddressDTO handleAddress(String email, AddressDTO addressDTO);

    Optional<AdminUserDTO> uploadImage(String email, MultipartFile image);

    Optional<AdminUserDTO> updateProfileInformation(AdminUserDTO userDTO);

    void changePassword(PasswordChangeDTO passwordChangeDTO);
}
