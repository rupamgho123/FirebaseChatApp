package com.coolapps.firebasechatdemo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.coolapps.firebasechatdemo.FirebaseConstants.TABLE_CHANNELS;
import static com.coolapps.firebasechatdemo.FirebaseConstants.TABLE_MESSAGES;

/**
 * Created by rupam.ghosh on 28/08/17.
 */

public class DatabaseReferenceHelper {
    public static DatabaseReference getNotificationDatabaseRef(String userId){
        return FirebaseDatabase.getInstance().getReference().child("notifications").child(userId);
    }

    public static DatabaseReference getMessageOfChannel(String channelId){
        return FirebaseDatabase.getInstance().getReference().child(TABLE_MESSAGES)
                .child(channelId).child(TABLE_MESSAGES);
    }

    public static DatabaseReference getChannelsDatabaseRef(){
        return FirebaseDatabase.getInstance().getReference().child(TABLE_CHANNELS);
    }

    public static DatabaseReference getChannelOfUserDatabaseRef(String userId){
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(userId)
                .child(TABLE_CHANNELS);
    }
}
