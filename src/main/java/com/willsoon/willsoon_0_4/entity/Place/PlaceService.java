package com.willsoon.willsoon_0_4.entity.Place;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<PlaceMarkerPojo> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(place -> new PlaceMarkerPojo(
                        place.getId(),
                        place.getName(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getAverageRating().toString(),
                        place.getDescription(),
                        place.getAddress()
                ))
                .toList();
    }

    public PlacePojo getPlaceById(UUID placeId) throws Exception {
        Place place =  placeRepository.findById(placeId).orElseThrow(()-> new Exception("Place Not Found"));

        return new PlacePojo(
                place.getMainPicture(),
                place.getName(),
                place.getAverageRating().toString(),
                place.getDescription(),
                place.getAddress()
        );
    }

}
