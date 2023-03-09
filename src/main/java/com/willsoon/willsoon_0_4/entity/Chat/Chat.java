package com.willsoon.willsoon_0_4.entity.Chat;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.BaseEntity;
import com.willsoon.willsoon_0_4.entity.Message.Message;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "chat")
public class Chat extends BaseEntity {

    /*@ManyToMany
    @JoinTable(name = "chat_users",
            joinColumns = {@JoinColumn(name = "chat_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<AppUser> users = new ArrayList<>();*/

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private AppUser sender;

    @OneToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private AppUser recipient;
}
