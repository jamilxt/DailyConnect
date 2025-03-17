package dev.jamilxt.dailytasktracking.controller.web;

import dev.jamilxt.dailytasktracking.model.ChatMessage;
import dev.jamilxt.dailytasktracking.servie.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message) {
        chatService.addMessage(message);
        if ("PRIVATE".equals(message.getType())) {
            messagingTemplate.convertAndSendToUser(message.getTarget(), "/queue/private", message);
            messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/private", message);
        }
        messagingTemplate.convertAndSend("/topic/onlineUsers", chatService.getOnlineUsers());
    }

    @GetMapping("/chat/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@RequestParam("target") String target, Authentication authentication) {
        String currentUser = authentication.getName();
        return chatService.getChatHistory(currentUser, target);
    }

    @GetMapping("/chat")
    public String chatPage(Authentication authentication, Model model) {
        String currentUser = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())
                ? authentication.getName() : "Guest";
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("onlineUsers", chatService.getOnlineUsers());
        model.addAttribute("historyUsers", chatService.getChatHistoryUsers(currentUser));
        return "chat";
    }
}