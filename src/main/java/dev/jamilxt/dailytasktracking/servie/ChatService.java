package dev.jamilxt.dailytasktracking.servie;

import dev.jamilxt.dailytasktracking.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    // Online users (username -> connection timestamp)
    private final Map<String, Long> onlineUsers = new ConcurrentHashMap<>();

    // Chat history (username -> list of messages)
    private final Map<String, List<ChatMessage>> chatHistory = new ConcurrentHashMap<>();

    public void userConnected(String username) {
        onlineUsers.put(username, System.currentTimeMillis());
    }

    public void userDisconnected(String username) {
        onlineUsers.remove(username);
    }

    public List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.keySet());
    }

    public void addMessage(ChatMessage message) {
        String key1 = message.getSender() + "_" + message.getTarget();
        String key2 = message.getTarget() + "_" + message.getSender();

        // Store for sender
        chatHistory.computeIfAbsent(key1, k -> new ArrayList<>()).add(message);
        // Store for target (if private)
        if ("PRIVATE".equals(message.getType())) {
            chatHistory.computeIfAbsent(key2, k -> new ArrayList<>()).add(message);
        }
    }

    public List<ChatMessage> getChatHistory(String user1, String user2) {
        String key = user1 + "_" + user2;
        return chatHistory.getOrDefault(key, Collections.emptyList());
    }

    public List<String> getChatHistoryUsers(String currentUser) {
        return chatHistory.keySet().stream()
                .filter(key -> key.startsWith(currentUser + "_"))
                .map(key -> key.substring(currentUser.length() + 1))
                .distinct()
                .collect(Collectors.toList());
    }
}