package com.egitim.demo.controller;

import com.egitim.demo.dto.UserCreateRequest;
import com.egitim.demo.dto.UserResponse;
import com.egitim.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users   Body: {"username":"bedirhan","email":"b@ornek.com","password":"gizli123"}
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest request) {
        if (!userService.isValid(request)) {
            return ResponseEntity.badRequest().build();                  // 400
        }
        if (userService.usernameOrEmailTaken(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();   // 409
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request)); // 201
    }

    // GET /api/users
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    // GET /api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
