package com.willsoon.willsoon_0_4.entity.Place;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface PlaceRepository extends JpaRepository<Place, UUID> {

}
