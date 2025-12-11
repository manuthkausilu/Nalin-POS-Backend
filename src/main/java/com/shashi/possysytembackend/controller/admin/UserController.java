package com.shashi.possysytembackend.controller.admin;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.UserDTO;
import com.shashi.possysytembackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("adminUserController")
@RequestMapping("/api/admin/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserDTO user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        Response response = userService.updateUser(id, userDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
