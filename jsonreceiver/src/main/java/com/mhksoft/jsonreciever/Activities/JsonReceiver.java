package com.mhksoft.jsonreciever.Activities;

import android.content.Context;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import co.ronash.pushe.Pushe;

public class JsonReceiver {
    static InterstitialAd interstitialAd;
    static RewardedVideoAd rewardedVideoAd;
    static String adMobAppId;
    static String adMobInterstitialId;
    static String adMobVideoId;
    static String adUnityId;

    public static void initializeNotifications(Context mContext) {
        // Pushe
        Pushe.initialize(mContext, true);

        // One Signal
        OneSignal
                .startInit(mContext)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();

        // Firebase
        FirebaseApp
                .initializeApp(mContext);

        FirebaseMessaging
                .getInstance()
                .subscribeToTopic("all");
    }

    public static void initializeAds(Context mContext, String adMobAppId, String adMobInterstitialId, String adMobVideoId, String adUnityId) {
        JsonReceiver.adMobAppId = adMobAppId;
        JsonReceiver.adMobInterstitialId = adMobInterstitialId;
        JsonReceiver.adMobVideoId = adMobVideoId;
        JsonReceiver.adUnityId = adUnityId;

        MobileAds.initialize(mContext, adMobAppId);
        interstitialAd = new InterstitialAd(mContext);
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
    }

    public static void initializeAll(Context mContext, String adMobAppId, String adMobInterstitialId, String adMobVideoId, String adUnityId) {
        JsonReceiver.adMobAppId = adMobAppId;
        JsonReceiver.adMobInterstitialId = adMobInterstitialId;
        JsonReceiver.adMobVideoId = adMobVideoId;
        JsonReceiver.adUnityId = adUnityId;

        // Pushe
        Pushe.initialize(mContext, true);

        // One Signal
        OneSignal
                .startInit(mContext)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();

        // Firebase
        FirebaseApp
                .initializeApp(mContext);

        FirebaseMessaging
                .getInstance()
                .subscribeToTopic("all");

        // Admob
        MobileAds.initialize(mContext, adMobAppId);
        interstitialAd = new InterstitialAd(mContext);
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
    }
}
