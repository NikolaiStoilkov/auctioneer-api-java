package com.auctioneer.service.ad;

import com.auctioneer.dtos.ad.BidResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages Server-Sent Event connections for live bid updates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BidSseService {
    private final ObjectMapper objectMapper;
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final List<SseEmitter> globalEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe(Long adId) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        emitters.computeIfAbsent(adId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        Runnable cleanup = () -> removeEmitter(adId, emitter);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> cleanup.run());

        return emitter;
    }

    public SseEmitter subscribeGlobal() {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        globalEmitters.add(emitter);

        Runnable cleanup = () -> globalEmitters.remove(emitter);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> cleanup.run());

        return emitter;
    }

    public void broadcast(Long adId, BidResponseDto payload) {
        String json;

        try {
            json = objectMapper.writeValueAsString(payload);
        } catch (IOException e) {
            log.error("Failed to serialize bid payload", e);
            return;
        }

        sendToEmitters(emitters.getOrDefault(adId, List.of()), adId, json);
        sendToGlobalEmitters(json);
    }

    private void sendToEmitters(List<SseEmitter> targets, Long adId, String json) {
        List<SseEmitter> dead = new ArrayList<>();

        for (SseEmitter emitter : targets) {
            try {
                emitter.send(SseEmitter.event().name("bid").data(json));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }

        dead.forEach(e -> removeEmitter(adId, e));
    }

    private void sendToGlobalEmitters(String json) {
        List<SseEmitter> dead = new ArrayList<>();

        for (SseEmitter emitter : globalEmitters) {
            try {
                emitter.send(SseEmitter.event().name("bid").data(json));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }

        dead.forEach(globalEmitters::remove);
    }

    private void removeEmitter(Long adId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(adId);

        if (list != null) list.remove(emitter);
    }
}

