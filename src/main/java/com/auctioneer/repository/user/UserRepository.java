package com.auctioneer.repository.user;

import com.auctioneer.domain.entities.User;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdForUpdate(Long id);
}
