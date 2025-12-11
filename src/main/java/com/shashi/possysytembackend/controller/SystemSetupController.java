package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setup")
@CrossOrigin
public class SystemSetupController {
    private final UserService userService;

    public SystemSetupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create-admin")
    public ResponseEntity<Response> createInitialAdmin() {
        Response response = userService.createInitialAdminUser();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
