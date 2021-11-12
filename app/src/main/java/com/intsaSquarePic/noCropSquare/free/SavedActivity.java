package com.intsaSquarePic.noCropSquare.free;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyZone;
import com.airbnb.lottie.LottieAnimationView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class SavedActivity extends AppCompatActivity {

    ImageView img, home;
    ImageView share;
    LottieAnimationView shareinsta, sharewp, sharefb, sharebutt;

    private final String APP_ID = "app801b6402e4b741ad8b";
    private final String ZONE_ID = "vzc8fd799f505d4d4c9c";
    private final String TAG = "AdColonyBannerDemo";

    private AdColonyAdViewListener listener;
    private AdColonyAdOptions adOptions;
    private RelativeLayout adContainer;
    private AdColonyAdView adView;

    private RelativeLayout adContainer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        AdsClass.AdColonyShow(SavedActivity.this);
        adContainer = findViewById(R.id.ad_container2);

        listener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                Log.d(TAG, "onRequestFilled");

                adContainer.addView(adColonyAdView);
                adView = adColonyAdView;
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
                Log.d(TAG, "onRequestNotFilled");

            }

            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
                Log.d(TAG, "onClosed");
            }

            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
                Log.d(TAG, "onClicked");
            }

            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
                Log.d(TAG, "onLeftApplication");
            }
        };
        requestBannerAd();

        AdColonyAppOptions appOptions = new AdColonyAppOptions();
        AdColony.configure(this, appOptions, APP_ID, ZONE_ID);

        try {


            AdsClass.getInstance().getmopubInstance(SavedActivity.this).setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                @Override
                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                }

                @Override
                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                    AdsClass.getInstance().getmopubInstance(SavedActivity.this).load();

                }

                @Override
                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {


                }
            });


            if (!AdsClass.getInstance().getmopubInstance(SavedActivity.this).isReady()) {
                AdsClass.getInstance().getmopubInstance(SavedActivity.this).load();
            } else {
                AdsClass.getInstance().getmopubInstance(SavedActivity.this).show();

            }
        } catch (Exception e) {
            Toast.makeText(SavedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        img = findViewById(R.id.imageviewww);
        home = findViewById(R.id.homebutt);
        sharebutt = findViewById(R.id.sharebutt);

        sharefb = findViewById(R.id.sharefb);
        shareinsta = findViewById(R.id.shareinsta);
        sharewp = findViewById(R.id.sharewp);

        img.setImageBitmap(utils.bit);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SavedActivity.this, imagePick_Activity.class);
                    startActivity(intent);
                } catch (Exception exception) {
                    Log.d(TAG, "onClick: " + exception.getMessage());
                }

            }
        });

        sharebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), utils.bit, "Title", null);
                Uri imageUri = Uri.parse(path);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
            }
        });

        sharewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharevia("com.whatsapp");
            }
        });

        shareinsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharevia("com.instagram.android");
            }
        });

        sharefb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharevia("com.facebook.katana");
            }
        });

    }

    private void requestBannerAd() {
        // Optional Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions();
        AdColony.requestAdView(ZONE_ID, listener, AdColonyAdSize.BANNER, adOptions);
    }

    private void homeclick() {
    }

    private void sharevia(String packagae) {
        String path = null;
        // String path = null;
        //  path = MediaStore.Images.Media.insertImage(getContentResolver(), utils.arealayoutbit, "Title", null);
        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), utils.bit, "title", null);
        Uri imageUri = Uri.parse(pathofBmp);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setPackage(packagae);
        //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));

    }

    @Override
    public void onBackPressed() {
        homeclick();
        startActivity(new Intent(getApplicationContext(), imagePick_Activity.class));
    }

}




//
//        if (adContainer2.getChildCount() > 0) {
//            adContainer2.removeView(adView);
//        }
//
//
//
//        listener = new AdColonyAdViewListener() {
//            @Override
//            public void onRequestFilled(AdColonyAdView adColonyAdView) {
//                Log.d(TAG, "onRequestFilled");
//
//                adContainer2.addView(adColonyAdView);
//                adView = adColonyAdView;
//            }
//
//            @Override
//            public void onRequestNotFilled(AdColonyZone zone) {
//                super.onRequestNotFilled(zone);
//                Log.d(TAG, "onRequestNotFilled");
//
//            }
//
//            @Override
//            public void onOpened(AdColonyAdView ad) {
//                super.onOpened(ad);
//                Log.d(TAG, "onOpened");
//            }
//
//            @Override
//            public void onClosed(AdColonyAdView ad) {
//                super.onClosed(ad);
//                Log.d(TAG, "onClosed");
//            }
//
//            @Override
//            public void onClicked(AdColonyAdView ad) {
//                super.onClicked(ad);
//                Log.d(TAG, "onClicked");
//            }
//
//            @Override
//            public void onLeftApplication(AdColonyAdView ad) {
//                super.onLeftApplication(ad);
//                Log.d(TAG, "onLeftApplication");
//            }
//        };
//        requestBannerAd();
//
////        mInterstitialAd = new InterstitialAd(this);
////        mInterstitialAd.setAdUnitId(getString(R.string.Interstitial_ads));
////        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        // MoPub ads interstitial
//
//
//
//       img  = findViewById(R.id.imageviewww);
//       home = findViewById(R.id.homebutt);
//        sharebutt = findViewById(R.id.sharebutt);
//
//        sharefb = findViewById(R.id.sharefb);
//        shareinsta = findViewById(R.id.shareinsta);
//        sharewp = findViewById(R.id.sharewp);
//
//        img.setImageBitmap(utils.bit);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            Intent intent = new Intent(SavedActivity.this, imagePick_Activity.class);
//                startActivity(intent);
//            }
//        });
//
//        sharebutt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String path = MediaStore.Images.Media.insertImage(getContentResolver(), utils.bit, "Title", null);
//                Uri imageUri = Uri.parse(path);
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                sharingIntent.setType("image/*");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
//            }
//        });
//
//        sharewp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharevia("com.whatsapp");
//            }
//        });
//
//        shareinsta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharevia("com.instagram.android");
//            }
//        });
//
//        sharefb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharevia("com.facebook.katana");
//            }
//        });
//    }
//
//    private void requestBannerAd() {
//
//        // Optional Ad specific options to be sent with request
//        adOptions = new AdColonyAdOptions();
//
//        //Request Ad
//        AdColony.requestAdView(ZONE_ID, listener, AdColonyAdSize.BANNER, adOptions);
//    }
//
//    private void homeclick() {
////        mInterstitialAd.setAdListener(new AdListener() {
////            @Override
////            public void onAdLoaded() {
////                 Code to be executed when an ad finishes loading.
////                startActivity(new Intent(getApplicationContext(),imagePick_Activity.class));
////                finish();
////            }
////
////            @Override
////            public void onAdFailedToLoad(int errorCode) {
////                 Code to be executed when an ad request fails.
////                startActivity(new Intent(getApplicationContext(),imagePick_Activity.class));
////                finish();
//            }
//
////            @Override
////            public void onAdOpened() {
////                 Code to be executed when the ad is displayed.
////            }
////
////            @Override
////            public void onAdClicked() {
////                 Code to be executed when the user clicks on an ad.
////            }
//
////            @Override
////            public void onAdLeftApplication() {
////                // Code to be executed when the user has left the app.
////            }
//
////            @Override
////            public void onAdClosed() {
////                // Code to be executed when the interstitial ad is closed.
////                startActivity(new Intent(getApplicationContext(),imagePick_Activity.class));
////                finish();
////            }
////        });
////    }
//
//    private void sharevia(String packagae) {
//        String path = null;
//        // String path = null;
//        //  path = MediaStore.Images.Media.insertImage(getContentResolver(), utils.arealayoutbit, "Title", null);
//        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), utils.bit,"title", null);
//        Uri imageUri = Uri.parse(pathofBmp);
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setPackage(packagae);
//        //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        sharingIntent.setType("image/*");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
//
//    }
//    @Override
//    public void onBackPressed()
//    {
//        homeclick();
//
//        startActivity(new Intent(getApplicationContext(),imagePick_Activity.class));
//    }
//
//}
