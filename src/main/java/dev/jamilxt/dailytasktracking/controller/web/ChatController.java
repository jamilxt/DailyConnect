package dev.jamilxt.dailytasktracking.controller.web;

import dev.jamilxt.dailytasktracking.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        if (message.getType() == ChatMessage.MessageType.PRIVATE) {
            messagingTemplate.convertAndSendToUser(message.getTarget(), "/queue/private", message);
        } else if (message.getType() == ChatMessage.MessageType.GROUP) {
            messagingTemplate.convertAndSend("/topic/group/" + message.getTarget(), message);
        }
    }
}