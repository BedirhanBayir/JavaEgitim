package com.egitim.demo.service;

import com.egitim.demo.dto.UserCreateRequest;
import com.egitim.demo.dto.UserResponse;
import com.egitim.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1;

    public UserResponse createUser(UserCreateRequest request) {
        User newUser = new User(nextId++, request.getUsername(),
                                request.getEmail(), request.getPassword(), "USER");
        users.add(newUser);
        return toResponse(newUser);
    }

    public List<UserResponse> getAllUsers() {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(this::toResponse);
    }

    public boolean isValid(UserCreateRequest r) {
        return r.getUsername() != null && !r.getUsername().isBlank()
                && r.getEmail() != null && r.getEmail().contains("@")
                && r.getPassword() != null && r.getPassword().length() >= 6;
    }

    public boolean usernameOrEmailTaken(UserCreateRequest r) {
        return users.stream().anyMatch(u ->
                u.getUsername().equalsIgnoreCase(r.getUsername())
                || u.getEmail().equalsIgnoreCase(r.getEmail()));
    }

    // Entity → DTO dönüşümü. password bilerek kopyalanmıyor.
    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(),
                                user.getEmail(), user.getRole());
    }
}
