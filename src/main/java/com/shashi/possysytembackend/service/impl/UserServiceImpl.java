package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.LoginRequest;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.UserDTO;
import com.shashi.possysytembackend.entity.User;
import com.shashi.possysytembackend.exception.OurException;
import com.shashi.possysytembackend.repository.UserRepository;
import com.shashi.possysytembackend.service.UserService;
import com.shashi.possysytembackend.util.JWTUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response register(UserDTO userDto) {
        Response response = new Response();
        try {
            if (userDto.getRole() == null){
                userDto.setRole(User.Role.User);
            }
            if (userRepository.existsByUserName(userDto.getUserName())) {
                throw new OurException(userDto.getUserName() + " Already Exists");
            }

            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = modelMapper.map(savedUser, UserDTO.class);
            response.setStatusCode(201);
            response.setUserDTO(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Registration failed: " + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            User user = userRepository.findByUserNameAndIsActiveTrue(loginRequest.getUsername())
                    .orElseThrow(() -> new OurException("User not found"));
            System.out.println(user.getIsActive());
            String token = jwtUtils.generateToken(user, user.getUserId().toString());
            user.setPassword(null);
            response.setStatusCode(200);
            response.setToken(token);
//            response.setRole(String.valueOf(user.getRole()));

            response.setExpirationTime("7 Days");
            response.setUserDTO(modelMapper.map(user, UserDTO.class));
            response.setMessage("login successful");
        } catch (BadCredentialsException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid username or password");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Login failed: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(Long id) {
        Response response = new Response();
        try {
            User user = userRepository.findById(id.intValue()).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            response.setUserDTO(userDto);
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOList = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
            response.setUserDTOList(userDTOList);
            response.setStatusCode(200);
            response.setMessage("Users fetched successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching users: " + e.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public Response updateUser(Integer id, UserDTO userDTO) {
        Response response = new Response();
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new OurException("User not found"));

            // Username update with duplicate check (ignore if same as current)
            if (userDTO.getUserName() != null && !userDTO.getUserName().isEmpty()) {
                if (!userDTO.getUserName().equals(existingUser.getUsername()) &&
                        userRepository.existsByUserName(userDTO.getUserName())) {
                    throw new OurException("Username already exists");
                }
                existingUser.setUserName(userDTO.getUserName());
            }

            // Password update
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                System.out.println("password : "+userDTO.getPassword());
            }

            // Email update
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                existingUser.setEmail(userDTO.getEmail());
            }

            // Role update only if not null
            if (userDTO.getRole() != null) {
                existingUser.setRole(userDTO.getRole());
            }

            // isActive update only if not null
            if (userDTO.getIsActive() != null) {
                existingUser.setIsActive(userDTO.getIsActive());
            }

            User updatedUser = userRepository.save(existingUser);
            UserDTO updatedUserDto = modelMapper.map(updatedUser, UserDTO.class);

            response.setUserDTO(updatedUserDto);
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(Integer id) {
        Response response = new Response();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new OurException("User not found"));
            userRepository.delete(user);
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response changePassword(Long userId, String oldPassword, String newPassword) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId.intValue())
                    .orElseThrow(() -> new OurException("User not found"));

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                response.setStatusCode(400);
                response.setMessage("Previous password is incorrect");
                return response;
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            response.setStatusCode(200);
            response.setMessage("Password changed successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error changing password: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createInitialAdminUser() {
        Response response = new Response();
        try {
            // Check if an admin user exists
            Optional<User> existingAdminOpt = userRepository.findAll().stream()
                    .filter(user -> user.getRole() == User.Role.Admin)
                    .findFirst();

            if (existingAdminOpt.isPresent()) {
                User existingAdmin = existingAdminOpt.get();
                existingAdmin.setPassword(passwordEncoder.encode("admin123")); // Reset to default password
                userRepository.save(existingAdmin);

                response.setStatusCode(200);
                response.setMessage("Admin user already existed. Password updated to default (admin123).");
                response.setUserDTO(modelMapper.map(existingAdmin, UserDTO.class));
            } else {
                // Create new admin user
                User admin = new User();
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Default password
                admin.setEmail("admin@system.local");
                admin.setRole(User.Role.Admin);
                admin.setIsActive(true);

                User saved = userRepository.save(admin);
                response.setStatusCode(201);
                response.setMessage("Initial admin user created. Username: admin, Password: admin123");
                response.setUserDTO(modelMapper.map(saved, UserDTO.class));
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating/updating admin user: " + e.getMessage());
        }
        return response;
    }


}
