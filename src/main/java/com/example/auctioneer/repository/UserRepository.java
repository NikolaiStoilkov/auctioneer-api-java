package com.example.auctioneer.repository;

import com.example.auctioneer.domain.entities.User;
import org.springframework.data.repository.CrudRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    UserDetails findUsersByUsername(String username);
}
