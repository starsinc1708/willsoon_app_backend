package com.willsoon.willsoon_0_4.entity.AppUser;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    void enableAppUser(String email);

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT u FROM AppUser u " +
            "INNER JOIN Friendship f ON u.id = CASE " +
            "WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
            "WHERE f.user1.id = :userId OR f.user2.id = :userId")
    List<AppUser> findFriendsById(UUID userId);

    List<AppUser> findAllByEnabledTrue();

}
