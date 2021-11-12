package com.intsaSquarePic.noCropSquare.free;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class imagePick_Activity extends AppCompatActivity {

    private LinearLayout gallery_button_text;
    private static final String TAGB = "main";

    private int RESULT_LOAD_IMG = 10;
    private final String APP_ID = "app801b6402e4b741ad8b";
    private final String ZONE_ID = "vzc8fd799f505d4d4c9c";
    private final String TAG = "AdColonyBannerDemo";
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listenerI;
    private AdColonyAdOptions adOptionsI;

    private AdColonyAdViewListener listener;
    private AdColonyAdOptions adOptions;
    private AdColonyAdView adView;
    private RelativeLayout adContainer;
    private ProgressBar progressBar;

    LinearLayout showadsprogress;

    public static int ads = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        AdsClass.AdColonyload(imagePick_Activity.this);

        MoPubView moPubView = findViewById(R.id.ad_vew1);
        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
        moPubView.loadAd();

        gallery_button_text = (LinearLayout) findViewById(R.id.gallery_button_text);

        gallery_button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AdsClass.AdColonyShow(imagePick_Activity.this);
                getPhotoFromGallery();
            }
        });
    }

    private void showmopubads() {
        try {

            AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                @Override
                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                    Log.e(TAG, "onInterstitialLoaded: ");

                }

                @Override
                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                    AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();
                }

                @Override
                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
                    AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();

                }
            });


            if (!AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).isReady()) {
                AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();
            } else {
                AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).show();

            }
        } catch (Exception e) {
//                        customProgressDialog.dismiss();
            Toast.makeText(imagePick_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {

            }
        };
    }

    private void getPhotoFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        startActivityForResult(photoPickerIntent,RESULT_LOAD_IMG);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {

        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
                File file = new File(getApplicationContext().getCacheDir(), "images");
                file.mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file + "/image.jpg");
                decodeStream.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.close();
                changeScreen(FileProvider.getUriForFile(this, "com.intsaSquarePic.noCropSquare.free.fileprovider", new File(new File(getCacheDir(), "images"), "image.jpg")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else
        {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", 1).show();
        }

//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            final Uri contentUri;
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
//                cachePath.mkdirs(); // don't forget to make the directory
//                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg"); // overwrites this image every time
//                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                stream.close();
//                File imagePath = new File(this.getCacheDir(), "images");
//                File newFile = new File(imagePath, "image.jpg");
//                contentUri = FileProvider.getUriForFile(this, "com.jazzapp.squarepic.nocrop.squarefit.fileprovider", newFile);
//                changeScreen(contentUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
//        }
    }

    private void changeScreen(Uri uri) {

        Intent intent = new Intent(this, picinSquare_Activity.class);
        intent.putExtra("ImageUri", uri);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);
        Log.d("AdsTAG", "call");
    }
}






    //mopub
//    private void adsShow() {
//        if (ads==1)
//        {
//            showadsprogress.setVisibility(View.VISIBLE);
//        }
//
//        AdColonyAppOptions appOptions = new AdColonyAppOptions()
//                .setUserID("unique_user_id")
//                .setKeepScreenOn(true);
//
//        AdColony.configure(this, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");
//
//        adOptionsI = new AdColonyAdOptions();
//
//        listenerI = new AdColonyInterstitialListener() {
//
//            @Override
//            public void onRequestFilled(AdColonyInterstitial ad) {
//                imagePick_Activity  .this.ad = ad;
//                showadsprogress.setVisibility(View.GONE);
//                if (ads==1)
//                {
//                    ad.show();
//                    ads++;
//                }
//
//                Log.d("TAG1", "onRequestFilled");
//            }
//
//            @Override
//            public void onRequestNotFilled(AdColonyZone zone) {
//                // Ad request was not filled
//                showadsprogress.setVisibility(View.GONE);
//                Log.d("TAG", "onRequestNotFilled");
//                showmopubads();
//            }
//
//            @Override
//            public void onOpened(AdColonyInterstitial ad) {
//                // Ad opened, reset UI to reflect state change
//                showadsprogress.setVisibility(View.GONE);
//                Log.d("TAG", "onOpened");
//            }
//
//            @Override
//            public void onClosed(AdColonyInterstitial ad) {
//                super.onClosed(ad);
//                showadsprogress.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onExpiring(AdColonyInterstitial ad) {
//                // Request a new ad if ad is expiring
//                showadsprogress.setVisibility(View.GONE);
//                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
//                Log.d("TAG", "onExpiring");
//            }
//        };
//    }


//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//
//import androidx.annotation.LongDef;
//import androidx.annotation.NonNull;
//import androidx.core.content.FileProvider;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//import com.adcolony.sdk.AdColony;
//import com.adcolony.sdk.AdColonyAdOptions;
//import com.adcolony.sdk.AdColonyAdView;
//import com.adcolony.sdk.AdColonyAdViewListener;
//import com.adcolony.sdk.AdColonyAppOptions;
//import com.adcolony.sdk.AdColonyInterstitial;
//import com.adcolony.sdk.AdColonyInterstitialListener;
//import com.adcolony.sdk.AdColonyZone;
//import com.mopub.common.MoPub;
//import com.mopub.common.SdkConfiguration;
//import com.mopub.common.SdkInitializationListener;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;
//import com.mopub.mobileads.MoPubView;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//public class imagePick_Activity extends AppCompatActivity {
//
//    private LinearLayout gallery_button_text;
//    private static final String TAGB = "main";
//
//    private int RESULT_LOAD_IMG = 10;
//    private final String APP_ID = "app801b6402e4b741ad8b";
//    private final String ZONE_ID = "vzc8fd799f505d4d4c9c";
//    private final String TAG = "AdColonyBannerDemo";
//    private AdColonyInterstitial ad;
//    private AdColonyInterstitialListener listenerI;
//    private AdColonyAdOptions adOptionsI;
//
//    private AdColonyAdViewListener listener;
//    private AdColonyAdOptions adOptions;
//    private AdColonyAdView adView;
//    private RelativeLayout adContainer;
//    private ProgressBar progressBar;
//
//    LinearLayout showadsprogress;
//    public static int ads = 1;
////
////    private ImageView home_logo;
////    private Bitmap mainBitmap;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select);
//
//        showadsprogress = findViewById(R.id.showadsprogress);
//        if (ads >= 2) {
//            showadsprogress.setVisibility(View.GONE);
//        }
//
//        MoPubView moPubView = findViewById(R.id.ad_vew1);
//        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
//        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
//        moPubView.loadAd();
//
//
//        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);
//        adsShow();
//
//        //
//        gallery_button_text = findViewById(R.id.gallery_button_text);
//
//        gallery_button_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPhotoFromGallery();
//            }
//        });
//
//    }
//
//    private void showmopubads() {
//        try {
//
//            AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
//                @Override
//                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//
//                    Log.e(TAG, "onInterstitialLoaded: " );
//                }
//
//                @Override
//                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//                    AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();
//                    adsShow();
////                                customProgressDialog.dismiss();
//                }
//
//                @Override
//                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
//
//                }
//
//                @Override
//                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
//
//                }
//
//                @Override
//                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
//                    AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();
//
//                }
//            });
//
//
//            if (!AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).isReady()) {
//                AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).load();
//            } else {
//                AdsClass.getInstance().getmopubInstance(imagePick_Activity.this).show();
//
//            }
//        } catch (Exception e) {
////                        customProgressDialog.dismiss();
//            Toast.makeText(imagePick_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//    //MoPub
//    private SdkInitializationListener initSdkListener() {
//        return new SdkInitializationListener() {
//            @Override
//            public void onInitializationFinished() {
//
//            }
//        };
//    }
//
//    private void getPhotoFromGallery() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            final Uri contentUri;
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
//                cachePath.mkdirs(); // don't forget to make the directory
//                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg"); // overwrites this image every time
//                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                stream.close();
//                File imagePath = new File(this.getCacheDir(), "images");
//                File newFile = new File(imagePath, "image.jpg");
//                contentUri = FileProvider.getUriForFile(this, "com.jazzapp.squarepic.nocrop.squarefit.fileprovider", newFile);
//                changeScreen(contentUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void changeScreen(Uri imageUri) {
//
//        Intent i = new Intent(this, imagePick_Activity.class);
//        i.putExtra("ImageUri", imageUri);
//        startActivity(i);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);
//        Log.d("AdsTAG", "call");
//    }
//
//    //mopub
//    private void adsShow() {
//        if (ads == 1) {
//            showadsprogress.setVisibility(View.VISIBLE);
//        }
//
//        AdColonyAppOptions appOptions = new AdColonyAppOptions()
//                .setUserID("unique_user_id")
//                .setKeepScreenOn(true);
//
//        AdColony.configure(this, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");
//
//        adOptionsI = new AdColonyAdOptions();
//
//        listenerI = new AdColonyInterstitialListener() {
//
//            @Override
//            public void onRequestFilled(AdColonyInterstitial ad) {
//                imagePick_Activity.this.ad = ad;
//                showadsprogress.setVisibility(View.GONE);
//                if (ads == 1) {
//                    ad.show();
//                    ads++;
//                }
//
//                Log.d("TAG1", "onRequestFilled");
//            }
//
//            @Override
//            public void onRequestNotFilled(AdColonyZone zone) {
//                // Ad request was not filled
//                showadsprogress.setVisibility(View.GONE);
//                Log.d("TAG", "onRequestNotFilled");
//                showmopubads();
//            }
//
//            @Override
//            public void onOpened(AdColonyInterstitial ad) {
//                // Ad opened, reset UI to reflect state change
//                showadsprogress.setVisibility(View.GONE);
//                Log.d("TAG", "onOpened");
//            }
//
//            @Override
//            public void onClosed(AdColonyInterstitial ad) {
//                super.onClosed(ad);
//                showadsprogress.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onExpiring(AdColonyInterstitial ad) {
//                // Request a new ad if ad is expiring
//                showadsprogress.setVisibility(View.GONE);
//                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
//                Log.d("TAG", "onExpiring");
//            }
//        };
//
//    }
//}
//
////}
////
////
////
////        adsShow();
////        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerS, adOptionsS);
////
////        //MoPub ads
////        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da").build();
////        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());
////
////        // for banner ads
////        MoPubView moPubView = (MoPubView) findViewById(R.id.ad_vew);
////        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da"); // Enter your Ad Unit ID from www.mopub.com
////        moPubView.loadAd();
////    }
////
////    private SdkInitializationListener initSdkListener() {
////        return new SdkInitializationListener() {
////            @Override
////            public void onInitializationFinished() {
////
////            }
////        };
////    }
////    public void nextAct(View view) {
////        startActivity(new Intent(this,picinSquare_Activity.class));
////    }
////
////
////    private void adsShow() {
////
////        AdColonyAppOptions appOptions = new AdColonyAppOptions()
////                .setUserID("unique_user_id")
////                .setKeepScreenOn(true);
////
////        AdColony.configure(this, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");
////
////        adOptionsS = new AdColonyAdOptions();
////
////        listenerS= new AdColonyInterstitialListener() {
////
////            @Override
////            public void onRequestFilled(AdColonyInterstitial ad) {
////                imagePick_Activity.this.ad = ad;
////
////                ad.show();
//////
////                Log.d("TAG1", "onRequestFilled");
////            }
////
////            @Override
////            public void onRequestNotFilled(AdColonyZone zone) {
////                // Ad request was not filled
////                Log.d("TAG", "onRequestNotFilled");
////            }
////
////            @Override
////            public void onOpened(AdColonyInterstitial ad) {
////                // Ad opened, reset UI to reflect state change
////
////                Log.d("TAG", "onOpened");
////            }
////
////            @Override
////            public void onClosed(AdColonyInterstitial ad) {
////                super.onClosed(ad);
////
////            }
////
////            @Override
////            public void onExpiring(AdColonyInterstitial ad) {
////
////                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsS);
////                Log.d("TAG", "onExpiring");
////            }
////        };
////    }
////
////    private void getPhotoFromGallery() {
////            Intent intent = new Intent("android.intent.action.PICK");
////            intent.setType("image/*");
////            startActivityForResult(intent, this.RESULT_LOAD_IMG);
////    }
////
////    /* access modifiers changed from: protected */
////    @SuppressLint("WrongConstant")
////    public void onActivityResult(int i, int i2, Intent intent) {
////        super.onActivityResult(i, i2, intent);
////        if (i2 == -1) {
////            try {
////                Bitmap decodeStream = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
////                File file = new File(getApplicationContext().getCacheDir(), "images");
////                file.mkdirs();
////                FileOutputStream fileOutputStream = new FileOutputStream(file + "/image.jpg");
////                decodeStream.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
////                fileOutputStream.close();
////                changeScreen(FileProvider.getUriForFile(this, "com.intsaSquarePic.noCropSquare.free.fileprovider", new File(new File(getCacheDir(), "images"), "image.jpg")));
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            } catch (IOException e2) {
////                e2.printStackTrace();
////            }
////        } else
////            {
////                Toast.makeText(getApplicationContext(), "You haven't picked Image", 1).show();
////            }
////    }
////
////    private void changeScreen(Uri uri) {
////        Intent intent = new Intent(this, picinSquare_Activity.class);
////        intent.putExtra("ImageUri", uri);
////        startActivity(intent);
////    }
////

////
////    @Override
////    protected void onResume() {
////        super.onResume();
////
////            Log.d("AdsTAG", "call");
////    }
////}