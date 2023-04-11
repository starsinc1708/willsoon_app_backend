package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.auth.AuthenticationResponse;
import com.willsoon.willsoon_0_4.auth.RefreshTokenService;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserPojo;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserRepository;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserService;
import com.willsoon.willsoon_0_4.security.config.ErrorResponse;
import com.willsoon.willsoon_0_4.security.config.JwtService;
import com.willsoon.willsoon_0_4.security.config.customExceptions.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public String extractEmailFromToken(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Refresh Token is Missing");
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        return userEmail;
    }

    @GetMapping("/userId")
    public ResponseEntity<?> getUserIdByEmail(@NonNull HttpServletRequest request) {
        AppUser curUser = userRepository.findByEmail(extractEmailFromToken(request)).get();
        AppUserPojo userPojo = new AppUserPojo(
                curUser.getId(),
                curUser.getDBUsername(),
                curUser.getEmail(),
                curUser.getActive()
        );
        return ResponseEntity.ok().body(userPojo);
    }

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
        AppUser curUser = userRepository.findByEmail(extractEmailFromToken(request)).get();
        return ResponseEntity.ok().body(userService.getUserFriends(curUser.getId()));
    }


    @GetMapping("/refresh")
    public AuthenticationResponse refreshTokens(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) {

        var user = userRepository.findByEmail(extractEmailFromToken(request))
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<?> getUser(@PathVariable String username) {
//        try {
//            AppUserPojo userPojo = userService.getUser(username);
//            return ResponseEntity.ok().body(userPojo);
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ErrorResponse("Username not Found"));
//        }
//    }

    @GetMapping("/")
    public ResponseEntity<?> getChatById(@RequestParam("id") String userId) {
        try {
            AppUserPojo userPojo = userService.getUserById(userId);
            return ResponseEntity.ok().body(userPojo);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("User Id not Found"));
        }
    }

    @PostMapping("/updateUsername/{newUsername}")
    public ResponseEntity<?> updateUsername(@PathVariable String newUsername, @NonNull HttpServletRequest request) {
        AppUser appUser = userRepository.findByEmail(extractEmailFromToken(request))
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        AppUserPojo userPojo;
        try {
            userPojo = userService.updateUsername(appUser.getDBUsername(), newUsername);
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Username already exists"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userPojo);
    }

    @PostMapping("/setStatus/")
    public ResponseEntity<?> updateStatus(@RequestParam("status") String status, @NonNull HttpServletRequest request) {
        AppUser appUser = userRepository.findByEmail(extractEmailFromToken(request))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        AppUserPojo userPojo = userService.updateStatus(appUser, status);
        return ResponseEntity.status(HttpStatus.OK).body(userPojo);
    }

}
