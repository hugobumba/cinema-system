package com.hugobumba.cinemasystem.auth;

import com.hugobumba.cinemasystem.auth.dtos.AuthResponseDTO;
import com.hugobumba.cinemasystem.auth.dtos.LoginRequestDTO;
import com.hugobumba.cinemasystem.auth.dtos.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody @Valid RegisterRequestDTO request) {
        return authService.register(request);
    }
}
