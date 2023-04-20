package com.willsoon.willsoon_0_4.entity.Place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PlacePojo {
    private String imgUri;
    private String establishmentName;
    private String averageRating;
    private String establishmentTextInfo;
    private String address;
}
