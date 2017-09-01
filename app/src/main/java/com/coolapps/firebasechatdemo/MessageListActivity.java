package com.coolapps.firebasechatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coolapps.firebasechatdemo.FirebaseConstants.FAKE_TABLE_READ;
import static com.coolapps.firebasechatdemo.FirebaseConstants.MESSAGE_NOTIFICATION_TITLE;
import static com.coolapps.firebasechatdemo.FirebaseConstants.MESSAGE_TYPE_TEXT;
import static com.firebase.ui.auth.AuthUI.GOOGLE_PROVIDER;

/**
 * Created by rupam.ghosh on 26/08/17.
 */

public class MessageListActivity extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private String channelId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        channelId = getIntent().getStringExtra("CHANNEL_ID");
        userId = getIntent().getStringExtra("USER_ID");
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            List<AuthUI.IdpConfig> providers = new ArrayList<>();
            providers.add(new AuthUI.IdpConfig.Builder(GOOGLE_PROVIDER).build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(providers)
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
             displayChatMessages();
        }

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                final String message = input.getText().toString();
                if(!TextUtils.isEmpty(message)) {
                    DatabaseReferenceHelper.getMessageOfChannel(channelId)
                            .push()
                            .setValue(new ChatMessage(message,
                                    userId, false)
                            );

                    // Clear the input
                    DatabaseReferenceHelper.getChannelsDatabaseRef().child(channelId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot != null) {
                                Channel channel = dataSnapshot.getValue(Channel.class);
                                if (channel != null && channel.getConfirmedUsers() != null) {
                                    sendNotificationToAllUsers(userId, message, channel.getConfirmedUsers());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    DatabaseReferenceHelper.getChannelsDatabaseRef().child(channelId).child(FAKE_TABLE_READ).push().setValue(System.currentTimeMillis());
                    input.setText("");
                }
            }
        });
    }

    private void sendNotificationToAllUsers(@NonNull String mainUser,@NonNull String message, @NonNull List<User> users) {
        if(users.size() > 0) {
            for(User user : users) {
                if(!user.getIdentifier().equalsIgnoreCase(mainUser)) {
                    DatabaseReferenceHelper
                            .getNotificationDatabaseRef(user.getIdentifier())
                            .push()
                            .setValue(new Notification(MESSAGE_NOTIFICATION_TITLE , mainUser, message, MESSAGE_TYPE_TEXT,
                                    System.currentTimeMillis(), 0));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MessageListActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, DatabaseReferenceHelper.getMessageOfChannel(channelId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}
