package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Authority;
import com.noglugo.mvp.domain.User;
import com.noglugo.mvp.repository.AuthorityRepository;
import com.noglugo.mvp.repository.UserRepository;
import com.noglugo.mvp.security.AuthoritiesConstants;
import com.noglugo.mvp.security.jwt.TokenProvider;
import com.noglugo.mvp.service.*;
import com.noglugo.mvp.service.dto.*;
import com.noglugo.mvp.web.rest.errors.BadRequestAlertException;
import com.noglugo.mvp.web.rest.vm.LoginVM;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.jhipster.security.RandomUtil;

@Service
public class AccountNoglugoServiceImpl implements AccountNoglugoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNoglugoServiceImpl.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

    private final StoreService storeService;

    private final MailService mailService;

    private final AddressService addressService;

    private final LocationService locationService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AccountNoglugoServiceImpl(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        CacheManager cacheManager,
        StoreService storeService,
        MailService mailService,
        AddressService addressService,
        LocationService locationService,
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
        this.storeService = storeService;
        this.mailService = mailService;
        this.addressService = addressService;
        this.locationService = locationService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Override
    public AdminUserDTO registerUser(RegisterDTO registerDTO) {
        userRepository
            .findOneByEmailIgnoreCase(registerDTO.getEmail())
            .ifPresent(existingUser -> {
                throw new EmailAlreadyUsedException();
            });

        if (registerDTO.getRole().equals(AuthoritiesConstants.VENDOR)) {
            if (registerDTO.getStoreDTO() == null) {
                throw new BadRequestAlertException("no-store", "Store information's must be provided in case of vendor", "400");
            }
        }

        String encryptedPassword = passwordEncoder.encode(registerDTO.getPassword());
        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setActivated(false);
        user.setLogin(registerDTO.getEmail());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encryptedPassword);
        user.setTelephone(registerDTO.getPhoneNumber());
        user.setLangKey("fr");
        user.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository
            .findById(registerDTO.getRole().equals(AuthoritiesConstants.CLIENT) ? AuthoritiesConstants.CLIENT : AuthoritiesConstants.VENDOR)
            .ifPresent(authorities::add);
        user.setAuthorities(authorities);
        User savedUser = userRepository.save(user);

        StoreDTO storeDTO = null;
        if (registerDTO.getRole().equals(AuthoritiesConstants.VENDOR)) {
            LocationDTO locationDTO = null;
            AddressDTO addressDTO = null;
            storeDTO = registerDTO.getStoreDTO();
            storeDTO.setUserId(user.getId());
            storeDTO = storeService.save(storeDTO);

            if (registerDTO.getStoreDTO().getAddressDTO() != null) {
                addressDTO = registerDTO.getStoreDTO().getAddressDTO();
                addressDTO.setStore(storeDTO);
                addressDTO = addressService.save(addressDTO);
                if (registerDTO.getStoreDTO().getAddressDTO().getLocationDTO() != null) {
                    locationDTO = registerDTO.getStoreDTO().getAddressDTO().getLocationDTO();
                    locationDTO.setAddress(addressDTO);
                    locationDTO = locationService.save(locationDTO);
                }
            }
        }
        mailService.sendActivationEmail(user);
        return new AdminUserDTO(savedUser);
    }

    @Override
    public JwtDTO authenticateUser(LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        return new JwtDTO(jwt);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
