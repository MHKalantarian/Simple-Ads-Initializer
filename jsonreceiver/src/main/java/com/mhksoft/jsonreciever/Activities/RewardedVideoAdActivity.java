package com.mhksoft.jsonreciever.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mhksoft.jsonreciever.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static com.mhksoft.jsonreciever.Activities.JsonReceiver.adMobVideoId;
import static com.mhksoft.jsonreciever.Activities.JsonReceiver.rewardedVideoAd;

public class RewardedVideoAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unity_ad);

        // Load Ad
        rewardedVideoAd.loadAd(adMobVideoId, new AdRequest.Builder().build());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                rewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                rewardedVideoAd.loadAd(adMobVideoId, new AdRequest.Builder().build());
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.e("Reward Video Error", String.valueOf(i));
                rewardedVideoAd.loadAd(adMobVideoId, new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });

        // Show Ad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideoAd.isLoaded())
                    rewardedVideoAd.show();
            }
        }, TimeUnit.MINUTES.toMillis(5));

    }

    @Override
    public void onBackPressed() {

    }
}
