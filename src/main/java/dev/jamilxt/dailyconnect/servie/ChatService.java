package dev.jamilxt.dailyconnect.servie;

import dev.jamilxt.dailyconnect.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final Map<String, Long> onlineUsers = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> chatHistory = new ConcurrentHashMap<>();

    public void userConnected(String username) {
        onlineUsers.put(username, System.currentTimeMillis());
        logger.debug("User connected: {}", username);
    }

    public void userDisconnected(String username) {
        onlineUsers.remove(username);
        logger.debug("User disconnected: {}", username);
    }

    public List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.keySet());
    }

    public void addMessage(ChatMessage message) {
        String[] users = new String[]{message.getSender(), message.getTarget()};
        Arrays.sort(users);
        String key = users[0] + "_" + users[1];
        logger.debug("Adding message to chatHistory: key={}", key);
        chatHistory.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
    }

    public List<ChatMessage> getChatHistory(String user1, String user2) {
        String[] users = new String[]{user1, user2};
        Arrays.sort(users);
        String key = users[0] + "_" + users[1];
        return chatHistory.getOrDefault(key, Collections.emptyList());
    }

    public List<String> getChatHistoryUsers(String currentUser) {
        Map<String, ChatMessage> lastMessages = new HashMap<>();
        chatHistory.forEach((key, messages) -> {
            if (key.startsWith(currentUser + "_") || key.endsWith("_" + currentUser)) {
                String otherUser = key.startsWith(currentUser + "_")
                        ? key.substring(currentUser.length() + 1)
                        : key.substring(0, key.length() - currentUser.length() - 1);
                ChatMessage latestMessage = messages.stream()
                        .max(Comparator.comparing(ChatMessage::getTimestamp))
                        .orElse(null);
                if (latestMessage != null) {
                    lastMessages.put(otherUser, latestMessage);
                }
            }
        });

        List<String> users = lastMessages.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().getTimestamp().compareTo(e1.getValue().getTimestamp()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        logger.debug("Chat history keys for {}: {}", currentUser, chatHistory.keySet());
        logger.debug("Sorted history users for {}: {}", currentUser, users);
        return users;
    }
}