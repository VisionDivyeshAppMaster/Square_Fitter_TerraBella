package com.intsaSquarePic.noCropSquare.free;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        AdsClass.AdColonyShow(BlankActivity.this);


    }
}