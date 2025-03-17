package dev.jamilxt.dailytasktracking.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private String content;
    private String sender;
    private String target;
    private String type;
    private LocalDateTime timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String content, String sender, String target, String type) {
        this.content = content;
        this.sender = sender;
        this.target = target;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}