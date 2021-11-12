package com.intsaSquarePic.noCropSquare.free;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fenil on 22-12-2017.
 */

public class SavingProcess  {
    private Context TheThis;
    private String NameOfFolder = "/Inst Fitter";
    private String NameOfFile = "InstFitter";

    public void SaveImage(Context context, Bitmap ImageToSave, int Quality) {
        TheThis = context;

        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + NameOfFolder;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, NameOfFile + "_" + CurrentDateAndTime + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, Quality, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        } catch (FileNotFoundException e) {
            UnableToSave();
        } catch (IOException e) {
            UnableToSave();
        }
    }





//    private void saveImage(Bitmap bitmap2) {
//        Bitmap bitmap = bitmap2;
//        OutputStream output;
//
//        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                .getAbsolutePath() + "/" + getResources().getString(R.string.app_name));
//        dir.mkdirs();
//
//        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String FileName = ts + ".jpg";
//        File file = new File(dir, FileName);
//        file.renameTo(file);
//        String uri = "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                .getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/" + FileName;
//
//        //for share image
//        url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                .getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/" + FileName;
//        try {
//            output = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//            output.flush();
//            output.close();
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(uri))));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }











    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage", "Scanned" + path + ":");
                        Log.e("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "Picture cannot be saved to gallery", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Picture saved to gallery", Toast.LENGTH_SHORT).show();

    }

}
