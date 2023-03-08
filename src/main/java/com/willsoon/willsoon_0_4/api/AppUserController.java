package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.auth.AuthenticationResponse;
import com.willsoon.willsoon_0_4.auth.RefreshTokenService;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserPojo;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserRepository;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserService;
import com.willsoon.willsoon_0_4.security.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AppUserController {
    private final AppUserService userService;

    private final AppUserRepository userRepository;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    @GetMapping("/allUsers")
    public ResponseEntity<List<AppUserPojo>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<AppUserPojo>> getUserFriends(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Refresh Token is Missing");
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        AppUser curUser = userRepository.findByEmail(userEmail).get();
        return ResponseEntity.ok().body(userService.getUserFriends(curUser.getId()));
    }


    @GetMapping("/refresh")
    public AuthenticationResponse refreshTokens(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Refresh Token is Missing");
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

}
