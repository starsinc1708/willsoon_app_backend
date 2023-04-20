package com.willsoon.willsoon_0_4.entity.Place;

import com.willsoon.willsoon_0_4.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place")
public class Place extends BaseEntity {

    @Column(name = "address")
    private String address;

    @Column(name = "mainPicture")
    private String mainPicture;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "average_check")
    private Double averageCheck;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private TypeOfPlace type;

    @Column(name = "description")
    private String description;



}
