package com.willsoon.willsoon_0_4.entity.Place;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
