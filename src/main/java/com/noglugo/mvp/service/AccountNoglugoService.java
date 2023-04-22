package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.AdminUserDTO;
import com.noglugo.mvp.service.dto.JwtDTO;
import com.noglugo.mvp.service.dto.RegisterDTO;
import com.noglugo.mvp.web.rest.vm.LoginVM;

public interface AccountNoglugoService {
    AdminUserDTO registerUser(RegisterDTO registerDTO);

    JwtDTO authenticateUser(LoginVM loginVM);
}
