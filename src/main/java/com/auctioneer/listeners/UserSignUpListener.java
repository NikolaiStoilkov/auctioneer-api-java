package com.auctioneer.listeners;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.UserDto;
import com.auctioneer.events.OnUserSignUpEvent;
import com.auctioneer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSignUpListener {
    private final UserRepository userRepository;

    @Async
    @EventListener
    public void handleUserRegistration(OnUserSignUpEvent event) {
        log.info("User registered:");
        log.info("User registered: {}", event.getUser().getUsername());

        User user = event.getUser();

        try { // What should be a better way to handle this?
            userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
