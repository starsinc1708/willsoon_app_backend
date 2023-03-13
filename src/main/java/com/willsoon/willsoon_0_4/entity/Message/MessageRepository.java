package com.willsoon.willsoon_0_4.entity.Message;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    Long countBySenderAndRecipientAndStatus(Optional<AppUser> sender, Optional<AppUser> recipient, MessageStatus status);

    @Query(value = "SELECT m.* FROM Message m WHERE m.chat_id = :chatId ORDER BY m.sent_at DESC LIMIT 1", nativeQuery = true)
    Message findLastMessageByChatId(UUID chatId);

    @Query(value = "SELECT * FROM message m WHERE m.chat_id = :chatId ORDER BY m.sent_at ASC OFFSET :offset ROWS FETCH NEXT 50 + :offset ROWS ONLY", nativeQuery = true)
    List<Message> findMessagesByChatIdAndSentAtWithOffset(UUID chatId, Integer offset);
}
