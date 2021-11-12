package com.intsaSquarePic.noCropSquare.free;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.mopub.mobileads.MoPubInterstitial;

public class AdsClass {

    private static AdsClass appAdOrganizer;
    private static MoPubInterstitial mInterstitial;

    public static AdColonyInterstitial adShow;
    private static AdColonyInterstitialListener listenerI;
    private static AdColonyAdOptions adOptionsI;

    private AdColonyAdViewListener listener;
    private static AdColonyAdOptions adOptions;
    private AdColonyAdView adView;

    public static boolean isFromSplash = false;

    public MoPubInterstitial getmopubInstance(Activity activity) {
        try {
            if (mInterstitial == null) {
                mInterstitial = new MoPubInterstitial(activity,"24534e1901884e398f1253216226017e");
            }
        } catch (Exception ignored) {
        }
        return mInterstitial;
    }

    public static void AdColonyload(Activity context){


        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);

        try{
            AdColony.configure(context, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");
        }catch (Exception e){
            Log.e("TAG", "AdColonyload: " );
        }


        adOptionsI = new AdColonyAdOptions();

        listenerI = new AdColonyInterstitialListener() {

            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                adShow = ad;
                Log.d("TAG1", "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled

                Log.d("TAG", "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change

                Log.d("TAG", "onOpened");
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);

                if(isFromSplash == true){
                    isFromSplash = false;
                    Intent intent = new Intent(context, imagePick_Activity.class);
                    context.startActivity(intent);
                }

            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
                Log.d("TAG", "onExpiring");
            }
        };

        AdColony.requestInterstitial(context.getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);

    }

    public static void AdColonyShow(Activity activity){
        if (adShow!=null){
            adShow.show();
        }
        AdColonyload(activity);
    }

    public static AdsClass getInstance() {
        AdsClass appAdOrganizer2 = appAdOrganizer;
        if (appAdOrganizer2 != null) {
            return appAdOrganizer2;
        }
        AdsClass appAdOrganizer3 = new AdsClass();
        appAdOrganizer = appAdOrganizer3;
        return appAdOrganizer3;
    }

}
