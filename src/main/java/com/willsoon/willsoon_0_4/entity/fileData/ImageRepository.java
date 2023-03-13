package com.willsoon.willsoon_0_4.entity.fileData;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageData, UUID> {

    Optional<ImageData> findByName(String name);

}
