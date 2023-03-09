package com.willsoon.willsoon_0_4.entity.Chat;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    Optional<Chat> findAllById(@NonNull UUID uuid);


    @Query("SELECT c from Chat c WHERE c.sender.id = :userId OR c.recipient.id = :userId")
    List<Chat> findAllBySenderOrRecipient(UUID userId);


    /*@Query("SELECT u.id from AppUser u JOIN Chat.users cu on u.id = cu.id " +
            "WHERE u.id <> :userId")
    UUID findRecipientIdBySenderId(@Param("userId") UUID userId);*/
}
