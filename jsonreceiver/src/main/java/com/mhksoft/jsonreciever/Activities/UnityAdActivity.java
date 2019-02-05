package com.mhksoft.jsonreciever.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.mhksoft.jsonreciever.R;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import androidx.appcompat.app.AppCompatActivity;

import static com.mhksoft.jsonreciever.Activities.JsonReceiver.adUnityId;

public class UnityAdActivity extends AppCompatActivity {
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unity_ad);
        mActivity = this;

        UnityAds.initialize(mActivity, adUnityId, new IUnityAdsListener() {
            @Override
            public void onUnityAdsReady(String placementId) {
                UnityAds.show(mActivity);
            }

            @Override
            public void onUnityAdsStart(String placementId) {

            }

            @Override
            public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {

            }

            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
                Log.e("Unity Ads Error", message);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
