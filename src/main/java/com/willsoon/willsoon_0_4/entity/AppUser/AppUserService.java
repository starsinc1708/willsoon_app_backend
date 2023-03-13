package com.willsoon.willsoon_0_4.entity.AppUser;

import com.willsoon.willsoon_0_4.entity.Chat.Chat;
import com.willsoon.willsoon_0_4.entity.Chat.ChatRepository;
import com.willsoon.willsoon_0_4.entity.Friendship.Friendship;
import com.willsoon.willsoon_0_4.entity.Friendship.FriendshipRepository;
import com.willsoon.willsoon_0_4.entity.Message.Message;
import com.willsoon.willsoon_0_4.entity.Message.MessageRepository;
import com.willsoon.willsoon_0_4.entity.Message.MessageStatus;
import com.willsoon.willsoon_0_4.registration.EmailValidator;
import com.willsoon.willsoon_0_4.registration.token.ConfirmationToken;
import com.willsoon.willsoon_0_4.registration.token.ConfirmationTokenService;
import com.willsoon.willsoon_0_4.security.config.customExceptions.EmailAlreadyExistsException;
import com.willsoon.willsoon_0_4.security.config.customExceptions.UsernameAlreadyExistsException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {

    private final  AppUserRepository appUserRepository;
    private final FriendshipRepository friendshipRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    @PostConstruct
    public void init() {
        AppUser user1 = new AppUser("shizobred12", "starsinc12@yandex.ru", bCryptPasswordEncoder.encode("password"), AppUserRole.USER);
        AppUser user2 = new AppUser("user2", "user2@example.com", bCryptPasswordEncoder.encode("password"), AppUserRole.USER);
        AppUser user3 = new AppUser("user3", "user3@example.com", bCryptPasswordEncoder.encode("password"), AppUserRole.USER);
        AppUser user4 = new AppUser("user4", "user4@example.com", bCryptPasswordEncoder.encode("password"), AppUserRole.USER);
        user1.setActive(true);
        user2.setActive(true);
        user3.setActive(false);
        user4.setActive(false);
        appUserRepository.save(user1);
        appUserRepository.enableAppUser(user1.getEmail());
        appUserRepository.save(user2);
        appUserRepository.enableAppUser(user2.getEmail());
        appUserRepository.save(user3);
        appUserRepository.enableAppUser(user3.getEmail());
        appUserRepository.save(user4);
        appUserRepository.enableAppUser(user4.getEmail());

        Friendship friendship1 = new Friendship(user1, user2);
        Friendship friendship2 = new Friendship(user2, user3);
        Friendship friendship3 = new Friendship(user3, user1);

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
        friendshipRepository.save(friendship3);

        Chat chat = new Chat(new ArrayList<>(), user1, user2);
        chatRepository.save(chat);

        Message message = new Message("ЭТО ВАЩЕ САМОЕ ПЕРВОЕ СООБЩЕНИЕ В ПРИЛОЖЕНИИ", chat, user1, user2, LocalDateTime.now(), MessageStatus.DELIVERED);
        messageRepository.save(message);

        Chat chat2 = new Chat(new ArrayList<>(), user1, user3);
        chatRepository.save(chat2);

        Message message2 = new Message("ЭТО ВАЩЕ САМОЕ ВТОРОЕ СООБЩЕНИЕ В ПРИЛОЖЕНИИ ЖЕСТЬ", chat2, user1, user3, LocalDateTime.now(), MessageStatus.DELIVERED);
        messageRepository.save(message2);

        Chat chat3 = new Chat(new ArrayList<>(), user1, user4);
        chatRepository.save(chat3);

        Message message3 = new Message("ЭТО ВАЩЕ САМОЕ ВТОРОЕ СООБЩЕНИЕ В ПРИЛОЖЕНИИ ЖЕСТЬ", chat3, user1, user4, LocalDateTime.now(), MessageStatus.DELIVERED);
        messageRepository.save(message3);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {

        boolean isValidEmail = emailValidator.test(appUser.getEmail());

        if(!isValidEmail) {
            throw new MailSendException("email not valid");
        }

        if(appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("email already taken");
        }

        if(appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("username already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUser.setCreatedDate(LocalDateTime.now());
        appUser.setLastModifiedDate(LocalDateTime.now());

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableAppUser(String email) {
        appUserRepository.enableAppUser(email);
    }

    public AppUser saveUser(AppUser user) {
        log.info("Saving new User {} to the Database", user.getUsername());
        return appUserRepository.save(user);
    }

    public List<AppUserPojo> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        return appUserRepository.findAllByEnabledTrue()
                .stream()
                .map(user -> new AppUserPojo(user.getId(), user.getDBUsername(), user.getUsername(), user.getEnabled()))
                .toList();
    }

    public List<AppUserPojo> getUserFriends(UUID userId) {
        return appUserRepository.findFriendsById(userId)
                .stream()
                .map(user -> new AppUserPojo(user.getId(), user.getDBUsername(), user.getUsername(), user.getEnabled()))
                .toList();
    }
}
