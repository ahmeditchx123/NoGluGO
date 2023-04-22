package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.service.ProfileNoglugoService;
import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.dto.AdminUserDTO;
import com.noglugo.mvp.service.dto.PasswordChangeDTO;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("api/v1/profile")
public class ProfileNoglugoResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileNoglugoResource.class);

    private final ProfileNoglugoService profileNoglugoService;

    public ProfileNoglugoResource(ProfileNoglugoService profileNoglugoService) {
        this.profileNoglugoService = profileNoglugoService;
    }

    @GetMapping(value = "me")
    public ResponseEntity<?> me() {
        LOGGER.debug("Rest to get profile information of the connected User ...");
        String email = getEmail();
        return ResponseEntity.ok().body(profileNoglugoService.profileInformation(email));
    }

    @PostMapping("address")
    public ResponseEntity<?> handleProfileAddress(@RequestBody AddressDTO addressDTO) {
        LOGGER.debug("Rest to handle address related to the connected user ...");
        String email = getEmail();
        return ResponseEntity.ok().body(profileNoglugoService.handleAddress(email, addressDTO));
    }

    @RequestMapping(value = "upload-profile-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        LOGGER.debug("Rest to upload profile image to the connected user...");
        String email = getEmail();
        return ResponseUtil.wrapOrNotFound(profileNoglugoService.uploadImage(email, image));
    }

    @PutMapping("update-infos")
    public ResponseEntity<?> updateProfileInfos(@RequestBody AdminUserDTO userDTO) {
        LOGGER.debug("Rest to update profile information of the connected user");
        return ResponseUtil.wrapOrNotFound(profileNoglugoService.updateProfileInformation(userDTO));
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChangeDTO passwordChangeDTO) {
        LOGGER.debug("Rest to update profile information of the connected user");
        profileNoglugoService.changePassword(passwordChangeDTO);
        return ResponseEntity.noContent().build();
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getUsername();
    }
}
