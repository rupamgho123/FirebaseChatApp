package com.coolapps.firebasechatdemo;

import java.util.Date;

/**
 * Created by rupam.ghosh on 23/08/17.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private boolean isRead;

    public ChatMessage(String messageText, String messageUser, boolean isRead) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
        this.isRead = isRead;
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}