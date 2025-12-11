package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.LoginRequest;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.UserDTO;
import com.shashi.possysytembackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = userService.getUserById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<Response> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Response response = userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
