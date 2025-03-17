package dev.jamilxt.dailytasktracking.model;

public class ChatMessage {
    private String content;
    private String sender;
    private String target;
    private MessageType type;

    public enum MessageType {
        PRIVATE, GROUP
    }

    public ChatMessage() {}
    public ChatMessage(String content, String sender, String target, MessageType type) {
        this.content = content;
        this.sender = sender;
        this.target = target;
        this.type = type;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
}