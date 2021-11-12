package com.intsaSquarePic.noCropSquare.free;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import java.io.IOException;

public class picinSquare_Activity extends AppCompatActivity {

    public static ImageView imageView;
    RelativeLayout SaveLayout;
    private int widthOfScreen;
    private int widthAndHeight;
    private float scale = 1f;
    private ScaleGestureDetector sgd;
    float halfImageView;
    private float widthAndHeightInDP;
    private int RESULT_LOAD_IMG;
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 0;
    ImageView prefButton;
    ImageView saveBtn, moreBtn;
    int setcout = 3;
    SharedPreferences dialogcout, oksure;
    boolean oksurek;
    SharedPreferences.Editor editor, okEditor;
    int count;

    // MoPub interstitial
    private MoPubInterstitial mInterstitial;

    // adColony interstitial
    private final String APP_ID = "app801b6402e4b741ad8b";
    private final String ZONE_ID = "vza6ef94e4656240dca5";
    private final String TAG = "AdColonyBannerDemo";
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listenerI;
    private AdColonyAdOptions adOptionsI;

    private AdColonyAdViewListener listener;
    private AdColonyAdOptions adOptions;
    private AdColonyAdView adView;

    private boolean fromSave = false;
    int addCpont = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptionsI);

        ImageView imageView1 = findViewById(R.id.OnBackapp);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsClass.AdColonyShow(picinSquare_Activity.this);
                finish();
            }
        });

        dialogcout = getPreferences(Context.MODE_PRIVATE);
        editor = dialogcout.edit();
        oksure = getPreferences(Context.MODE_PRIVATE);
        okEditor = oksure.edit();
        oksurek = oksure.getBoolean("okSure", true);
        if (oksurek) {
            count = dialogcout.getInt("counting", 0);
            count++;
            editor.putInt("counting", count);
            editor.commit();
        }

        prefButton = (ImageView) findViewById(R.id.pref5);
        imageView = (ImageView) findViewById(R.id.imageView1);

        widthAndHeight = getScreenWidth();
        widthAndHeightInDP = convertPixelsToDp(Math.round(widthAndHeight), this);
        halfImageView = widthAndHeightInDP / 2;
        SaveLayout = findViewById(R.id.saveLayout);
        saveBtn = findViewById(R.id.bt_save);
//        moreBtn = findViewById(R.id.bt_more);
        setImage();
        sgd = new ScaleGestureDetector(this, new ScaleListener());

        MoPubView moPubView = findViewById(R.id.ad_vew2);
        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
        moPubView.loadAd();
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
                picinSquare_Activity.this.ad = ad;

                if (addCpont == 1) {
                    addCpont = 2;
                    ad.show();
                }

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


    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
           /* MoPub SDK initialized.
           Check if you should show the consent dialog here, and make your ad requests. */
            }
        };
    }

    public void addImageClicked(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure select another image ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        picinSquare_Activity.super.onBackPressed();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void prefClicked(View view) {

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        imageView.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void setMoreBtn(View moreBtn) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You want to More Apps? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent iapps = new Intent("android.intent.action.VIEW");
                        iapps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=" + getResources().getString(R.string.developer)));
                        startActivity(iapps);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void pref4Clicked(View view) {

        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(-90.0f);
        Bitmap rotatedBitmap = Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.getWidth(), origBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
    }

    public void saveTextViewClicked(View view) {
        if (imageView.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "Please, First add an image to save.", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(picinSquare_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
                }
            } else {
                startSave();
            }
        }
    }

    public void padding1Clicked(View view) {
        imageView.setPadding(0, 0, 0, 0);
    }

    public void padding2Clicked(View view) {
        imageView.setPadding(40, 40, 40, 40);
    }

    public void padding3Clicked(View view) {
        imageView.setPadding(80, 80, 80, 80);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            Log.i("Scale:", Float.toString(scale));
            int padding = Math.round(halfImageView * (2 + (scale * -1)));
            imageView.setPadding(padding, padding, padding, padding);
            return true;
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float scale = context.getResources().getDisplayMetrics().densityDpi;
        float dp = px / (scale / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private void setImage() {
        widthOfScreen = getScreenWidth();
        Bundle ex = getIntent().getExtras();
        Uri imageUri = ex.getParcelable("ImageUri");
        RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(widthAndHeight, widthAndHeight);
        imageView.setLayoutParams(newParams);
        imageView.setBackgroundColor(getResources().getColor(R.color.white));
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageURI(imageUri);
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        return true;
    }

    CharSequence options[] = new CharSequence[]{"Same Quality", "Standard", "Normal"};

    private void collectionclick() {
    }

    public void startSave() {
        SaveLayout.setDrawingCacheEnabled(true);

        final Bitmap bitmap = Bitmap.createBitmap(SaveLayout.getDrawingCache());
        this.SaveLayout.destroyDrawingCache();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(this.options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                SavingProcess savingProcess = new SavingProcess();
                if (i == 0) {

                    //  add adColony

                    fromSave = true;
                    addCpont = 4;
                    saveImageQuality();

                } else if (i == 1) {


                    fromSave = true;
                    addCpont = 5;
                    saveImageQuality();

                } else if (i == 2) {

                    fromSave = true;
                    addCpont = 6;
                    saveImageQuality();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // code for saving image.
    private void saveImageQuality() {
        AdsClass.AdColonyShow(picinSquare_Activity.this);
        if (fromSave) {

            fromSave = false;

            picinSquare_Activity.this.SaveLayout.setDrawingCacheEnabled(true);
            final Bitmap createBitmap = Bitmap.createBitmap(picinSquare_Activity.this.SaveLayout.getDrawingCache());
            picinSquare_Activity.this.SaveLayout.destroyDrawingCache();

            SavingProcess savingProcess = new SavingProcess();

            if (addCpont == 4) {
                savingProcess.SaveImage(picinSquare_Activity.this, createBitmap, 100);

            } else if (addCpont == 5) {
                savingProcess.SaveImage(picinSquare_Activity.this, createBitmap, 80);

            } else if (addCpont == 6) {
                savingProcess.SaveImage(picinSquare_Activity.this, createBitmap, 50);
            }
            utils.bit = createBitmap;
            picinSquare_Activity.this.startActivity(new Intent(picinSquare_Activity.this.getApplicationContext(), SavedActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (count == setcout) {
            count = 0;
            editor.putInt("counting", count);
            editor.commit();
            dialog();
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure to Exit ?")
                    .setNeutralButton("Rate As", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                            new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(new_intent);
                        }
                    }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {

                    dialog.dismiss();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);

                }
            })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enjoying " + getResources().getString(R.string.app_name) + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogYes();
                    }
                })
                .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialogNotReally();
                    }
                })
                .show();
    }

    public void dialogNotReally() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you mind giving us some feedback?")
                .setPositiveButton("Ok, sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(new_intent);
                        okEditor.putBoolean("okSure", false);
                        okEditor.commit();
                    }
                })
                .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .show();
    }

    public void dialogYes() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How about a Rating on Play store, then?")
                .setPositiveButton("Ok, sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(new_intent);
                        okEditor.putBoolean("okSure", false);
                        okEditor.commit();
                    }
                })
                .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);

                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptions);
        Log.d("AdsTAG", "call");
    }
}