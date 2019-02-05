package com.mhksoft.jsonreciever.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.mhksoft.jsonreciever.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static com.mhksoft.jsonreciever.Activities.JsonReceiver.adMobInterstitialId;
import static com.mhksoft.jsonreciever.Activities.JsonReceiver.interstitialAd;

public class InterstitialAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unity_ad);
        interstitialAd.setAdUnitId(adMobInterstitialId);

        // Load Ad
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial Error", String.valueOf(i));
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        // Show Ad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interstitialAd.isLoaded())
                            interstitialAd.show();
                    }
                });
            }
        }, TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public void onBackPressed() {

    }
}
