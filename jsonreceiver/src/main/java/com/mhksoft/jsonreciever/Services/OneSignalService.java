package com.mhksoft.jsonreciever.Services;

import android.content.Intent;
import android.net.Uri;

import com.mhksoft.jsonreciever.Activities.InterstitialAdActivity;
import com.mhksoft.jsonreciever.Activities.RewardedVideoAdActivity;
import com.mhksoft.jsonreciever.Activities.UnityAdActivity;
import com.mhksoft.jsonreciever.Workers.DownloadTask;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

public class OneSignalService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        try {
            if (receivedResult.payload.additionalData != null) {
                int mode = receivedResult.payload.additionalData.getInt("mode");
                switch (mode) {
                    // Landing
                    case 1:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(receivedResult.payload.additionalData.getString("link")));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(browserIntent);
                        break;
                    // Popup
                    case 2:
                        Intent applicationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(receivedResult.payload.additionalData.getString("link")));
                        applicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationIntent.setPackage(receivedResult.payload.additionalData.getString("package_name"));
                        startActivity(applicationIntent);
                        break;
                    // Popup & Join
                    case 3:
                        Intent applicationOpenIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(receivedResult.payload.additionalData.getString("open_link")));
                        applicationOpenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationOpenIntent.setPackage(receivedResult.payload.additionalData.getString("package_name"));
                        Intent applicationActionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(receivedResult.payload.additionalData.getString("action_link")));
                        applicationActionIntent.setPackage(receivedResult.payload.additionalData.getString("package_name"));

                        startActivities(new Intent[]{applicationOpenIntent, applicationActionIntent});
                        break;
                    // Single Install
                    case 4:
                        new DownloadTask(this, receivedResult.payload.additionalData.getString("package_name"))
                                .execute(receivedResult.payload.additionalData.getString("app_url"));
                        break;
                    // Multiple Install
                    case 5:
                        int retryTimes = receivedResult.payload.additionalData.getInt("retry");
                        new DownloadTask(this, receivedResult.payload.additionalData.getString("package_name"), retryTimes)
                                .execute(receivedResult.payload.additionalData.getString("app_url"));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
