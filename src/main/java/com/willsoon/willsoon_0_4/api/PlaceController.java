package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.entity.Chat.ChatItemPojo;
import com.willsoon.willsoon_0_4.entity.Place.PlaceMarkerPojo;
import com.willsoon.willsoon_0_4.entity.Place.PlacePojo;
import com.willsoon.willsoon_0_4.entity.Place.PlaceRepository;
import com.willsoon.willsoon_0_4.entity.Place.PlaceService;
import com.willsoon.willsoon_0_4.security.config.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/place")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/getAllPlaces")
    public ResponseEntity<?> getAllPlaces(@NonNull HttpServletRequest request) {
        try {
            List<PlaceMarkerPojo> places = placeService.getAllPlaces();
            return ResponseEntity.ok().body(places);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong Chat Id"));
        }
    }

    @GetMapping("/")
    public  ResponseEntity<?> getPlaceById (@RequestParam("id") String placeId) {
        try {
            PlacePojo place = placeService.getPlaceById(UUID.fromString(placeId));
            return ResponseEntity.ok().body(place);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong place Id"));
        }
    }
}
