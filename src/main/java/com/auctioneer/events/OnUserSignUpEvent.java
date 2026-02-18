package com.auctioneer.events;

import com.auctioneer.domain.entities.User;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class OnUserSignUpEvent extends ApplicationEvent {
    private final User user;

    public OnUserSignUpEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
