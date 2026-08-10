package com.auctioneer.controller.user;

import com.auctioneer.exceptions.ExpiredTokenException;
import com.auctioneer.service.auth.JwtService;
import com.auctioneer.service.user.UserSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class NotificationController {

    private final UserSseService userSseService;
    private final JwtService jwtService;

    /**
     * SSE stream for per-user notifications.
     * Token is passed as a query parameter because EventSource cannot send headers.
     *
     * @param token the user's JWT
     * @return an emitter that pushes notification events for the user
     */
    @GetMapping(value = "/notifications/stream", produces = "text/event-stream")
    public SseEmitter stream(@RequestParam("token") String token) {
        if (jwtService.isTokenExpired(token)) {
            throw new ExpiredTokenException();
        }
        Long userId = Long.valueOf(jwtService.extractUserId(token));
        return userSseService.subscribe(userId);
    }
}
