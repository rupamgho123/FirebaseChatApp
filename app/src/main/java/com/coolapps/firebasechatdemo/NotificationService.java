package com.coolapps.firebasechatdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import static com.coolapps.firebasechatdemo.FirebaseConstants.*;


/**
 * Created by rupam.ghosh on 28/08/17.
 */

public class NotificationService extends Service {

    boolean startCalledOnce = false;
    String userId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStart(intent);
        return START_NOT_STICKY;
    }



    private void handleStart(Intent intent) {
        if(!startCalledOnce){
            startCalledOnce = true;
            userId = intent.getStringExtra(FirebaseConstants.USER_ID);
            DatabaseReferenceHelper.getNotificationDatabaseRef(userId)
                    .orderByChild(FirebaseConstants.STATUS).equalTo(0)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot != null){
                                Notification notification = dataSnapshot.getValue(Notification.class);

                                showNotification(getApplicationContext(),notification,dataSnapshot.getKey());
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void showNotification(Context context, Notification notification, String notification_key){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getDescription())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(Html.fromHtml(notification.getMessage()
                ))
                .setAutoCancel(true);

        Intent intent = new Intent(context, ChannelListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /*  Use the notification type to switch activity to stack on the main activity*/
        if(notification.getType().equals(NOTIFICATION_TYPE_MESSAGE)){
            intent = new Intent(context, MessageListActivity.class);
            intent.putExtra(FirebaseConstants.CHANNEL_ID,notification.getChannelId());
            intent.putExtra(FirebaseConstants.USER_ID,notification.getDescription());
        }


        final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                new Intent[] {intent}, PendingIntent.FLAG_ONE_SHOT);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChannelListActivity.class);

        mBuilder.setContentIntent(pendingIntent);


        NotificationManager mNotificationManager =  (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

        /* Update firebase set notifcation with this key to 1 so it doesnt get pulled by our notification listener*/
        flagNotificationAsSent(notification_key);
    }


    private void flagNotificationAsSent(String notification_key) {
        DatabaseReferenceHelper.getNotificationDatabaseRef(userId)
                .child(notification_key)
                .child(FirebaseConstants.STATUS)
                .setValue(1);
    }
}
