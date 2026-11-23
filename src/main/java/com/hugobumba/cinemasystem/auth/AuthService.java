package com.hugobumba.cinemasystem.auth;

import com.hugobumba.cinemasystem.auth.dtos.AuthResponseDTO;
import com.hugobumba.cinemasystem.auth.dtos.LoginRequestDTO;
import com.hugobumba.cinemasystem.auth.dtos.RegisterRequestDTO;
import com.hugobumba.cinemasystem.user.User;
import com.hugobumba.cinemasystem.user.UserRepository;
import com.hugobumba.cinemasystem.user.UserRole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return new AuthResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                "Bearer",
                jwtService.generateToken(user)
        );
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User saved = userRepository.save(user);

        return new AuthResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getRole(),
                "Bearer",
                jwtService.generateToken(saved)
        );
    }
}
