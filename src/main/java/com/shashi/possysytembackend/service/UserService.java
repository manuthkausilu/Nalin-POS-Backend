package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.LoginRequest;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.UserDTO;

public interface UserService {
    Response register(UserDTO userDto);
    Response login(LoginRequest loginRequest);
    Response getUserById(Long id);
    Response getAllUsers();
    Response updateUser(Integer id, UserDTO userDTO);
    Response deleteUser(Integer id);
    Response changePassword(Long userId, String oldPassword, String newPassword);
    Response createInitialAdminUser();
}
