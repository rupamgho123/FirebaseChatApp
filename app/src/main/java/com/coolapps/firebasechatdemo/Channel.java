package com.coolapps.firebasechatdemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rupam.ghosh on 26/08/17.
 */

public class Channel {

    public Channel(){

    }

    public Channel(User owner, HashMap<String,ChatMessage> messages, HashMap<String,User> confirmedUsers, HashMap<String,User> unConfirmedUsers) {
        this.owner = owner;
        this.messages = messages;
        this.confirmedUsers = confirmedUsers;
        this.unConfirmedUsers = unConfirmedUsers;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HashMap<String,ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String,ChatMessage> messages) {
        this.messages = messages;
    }

    public HashMap<String,User> getConfirmedUsers() {
        return confirmedUsers;
    }

    public void setConfirmedUsers(HashMap<String,User> confirmedUsers) {
        this.confirmedUsers = confirmedUsers;
    }

    public HashMap<String,User> getUnConfirmedUsers() {
        return unConfirmedUsers;
    }

    public void setUnConfirmedUsers(HashMap<String,User> unConfirmedUsers) {
        this.unConfirmedUsers = unConfirmedUsers;
    }

    private User owner;
    private HashMap<String,ChatMessage> messages;
    private HashMap<String,User> confirmedUsers;
    private HashMap<String,User> unConfirmedUsers;
}
