package com.coolapps.firebasechatdemo;

/**
 * Created by rupam.ghosh on 26/08/17.
 */

public class User {

    public User() {

    }

    public User(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    private String identifier;
}
