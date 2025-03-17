package dev.jamilxt.dailytasktracking.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class NotificationController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(@AuthenticationPrincipal UserDetails user) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(user.getUsername(), emitter);

        emitter.onCompletion(() -> emitters.remove(user.getUsername()));
        emitter.onTimeout(() -> emitters.remove(user.getUsername()));

        // Simulate a notification
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Delay for demo
                emitter.send(SseEmitter.event().name("notification").data("Welcome, " + user.getUsername() + "!"));
            } catch (IOException | InterruptedException e) {
                emitters.remove(user.getUsername());
            }
        }).start();

        return emitter;
    }
}