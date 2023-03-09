package com.willsoon.willsoon_0_4.entity.Message;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.BaseEntity;
import com.willsoon.willsoon_0_4.entity.Chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private AppUser sender;

    @OneToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private AppUser recipient;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "status")
    private MessageStatus status;

    public Message(String text, AppUser sender, LocalDateTime sentAt) {
        this.text = text;
        this.sender = sender;
        this.sentAt = sentAt;
    }
}
