package com.hugobumba.cinemasystem.user;

import com.hugobumba.cinemasystem.user.dtos.UserRequestDTO;
import com.hugobumba.cinemasystem.user.dtos.UserResponseDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO request) {
        User userEntity = toEntity(request);
        User createdUser = userService.createUser(userEntity);
        return toResponse(createdUser);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return toResponse(user);
    }

    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return toResponse(user);
    }

    @GetMapping("/role/{role}")
    public List<UserResponseDTO> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping
    public List<UserResponseDTO> listAllUsers() {
        return userService.listAllUsers()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        User userEntity = toEntity(request);
        userEntity.setId(id);
        User updatedUser = userService.updateUser(userEntity);
        return toResponse(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private User toEntity(UserRequestDTO request) {
        User userEntity = new User();
        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(request.getPassword());
        userEntity.setRole(request.getRole());
        return userEntity;
    }

    private UserResponseDTO toResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
