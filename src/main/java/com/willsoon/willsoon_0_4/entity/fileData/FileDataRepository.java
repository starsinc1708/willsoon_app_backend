package com.willsoon.willsoon_0_4.entity.fileData;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileDataRepository extends JpaRepository<FileData, UUID> {

    Optional<FileData> findByName(String fileName);

}
