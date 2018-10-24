package com.example.fragmento.biolabcloud;

public class ReponseMessage {

    String textMessage;
    boolean isMe;

    public ReponseMessage(String textMessage, boolean isMe) {
        this.textMessage = textMessage;
        this.isMe = isMe;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
