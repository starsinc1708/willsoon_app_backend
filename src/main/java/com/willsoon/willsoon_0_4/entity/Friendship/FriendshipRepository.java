package com.willsoon.willsoon_0_4.entity.Friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
}
