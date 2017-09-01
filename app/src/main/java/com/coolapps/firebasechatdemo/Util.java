package com.coolapps.firebasechatdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rupam.ghosh on 02/09/17.
 */

public class Util {

    @Nullable
    public static String getCurrentUserID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN",Context.MODE_PRIVATE);
        return sharedPreferences.getString(FirebaseConstants.USER_ID,null);
    }

    public static void setCurrentUserID(Context context,@NonNull String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FirebaseConstants.USER_ID,userId);
        editor.apply();
    }
}
