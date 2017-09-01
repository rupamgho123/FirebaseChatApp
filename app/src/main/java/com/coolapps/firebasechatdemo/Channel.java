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

    public Channel(String channelName, User owner, List<User> confirmedUsers, List<User> unConfirmedUsers) {
        this.channelName = channelName;
        this.owner = owner;
        this.confirmedUsers = confirmedUsers;
        this.unConfirmedUsers = unConfirmedUsers;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getConfirmedUsers() {
        return confirmedUsers;
    }

    public void setConfirmedUsers(List<User> confirmedUsers) {
        this.confirmedUsers = confirmedUsers;
    }

    public List<User> getUnConfirmedUsers() {
        return unConfirmedUsers;
    }

    public void setUnConfirmedUsers(List<User> unConfirmedUsers) {
        this.unConfirmedUsers = unConfirmedUsers;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    private String channelName;
    private User owner;
    private List<User> confirmedUsers;
    private List<User> unConfirmedUsers;
}
