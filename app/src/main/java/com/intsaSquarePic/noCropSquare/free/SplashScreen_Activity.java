package com.intsaSquarePic.noCropSquare.free;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;


public class SplashScreen_Activity extends AppCompatActivity {

    LinearLayout l1, l2;
    Animation uptodown, downupto;
    private final String APP_ID = "app801b6402e4b741ad8b";
    private final String ZONE_ID = "vza6ef94e4656240dca5";
    private final String TAG = "AdColonyBannerDemo";
    public static AdColonyInterstitial adShow;
    private AdColonyInterstitialListener listenerI;
    private AdColonyAdOptions adOptionsI;

    private AdColonyAdViewListener listener;
    private AdColonyAdOptions adOptions;
    private AdColonyAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdsClass.isFromSplash = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        AdsClass.AdColonyload(SplashScreen_Activity.this);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downupto = AnimationUtils.loadAnimation(this, R.anim.downupto);
        l2.setAnimation(downupto);
        l1.setAnimation(uptodown);

        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String myVersionName = "not available";
        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView tvVersionName = findViewById(R.id.versiontv);
        tvVersionName.setText("Version : " + myVersionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final SdkConfiguration.Builder SdkConfiguration = new SdkConfiguration.Builder("YOUR_AD_UNIT_ID");
                MoPub.initializeSdk(SplashScreen_Activity.this, SdkConfiguration.build(), initSdkListener());
            }
        }, 3000);
    }

    private SdkInitializationListener initSdkListener() {
        return () -> {
            if (!AdsClass.getInstance().getmopubInstance(SplashScreen_Activity.this).isReady()) {
                try {

                    AdsClass.getInstance().getmopubInstance(SplashScreen_Activity.this).setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                        @Override
                        public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {

                            Intent intent = new Intent(SplashScreen_Activity.this, BlankActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                            AdsClass.getInstance().getmopubInstance(SplashScreen_Activity.this).load();

                            Intent intent = new Intent(SplashScreen_Activity.this, BlankActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                        }

                        @Override
                        public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

                        }

                        @Override
                        public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
                            AdsClass.getInstance().getmopubInstance(SplashScreen_Activity.this).load();

                            Intent intent = new Intent(SplashScreen_Activity.this, BlankActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    AdsClass.getInstance().getmopubInstance(SplashScreen_Activity.this).load();


                } catch (Exception e) {
                    Toast.makeText(SplashScreen_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
//                AdsClass.AdColonyShow(imagePick_Activity.this);


                Intent intent = new Intent(SplashScreen_Activity.this, BlankActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private void adsShow() {

        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);

        AdColony.configure(this, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");

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

            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
                Log.d("TAG", "onExpiring");
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);
        Log.d("AdsTAG", "call");
    }
}
