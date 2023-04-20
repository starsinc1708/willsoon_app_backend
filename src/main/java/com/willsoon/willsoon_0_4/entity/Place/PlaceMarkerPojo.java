package com.willsoon.willsoon_0_4.entity.Place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PlaceMarkerPojo {
    private UUID id;
    private String name;
    private String latitude;
    private String longitude;
    private String averageRating;
    private String description;
    private String address;
}
