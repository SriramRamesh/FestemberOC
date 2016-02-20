package com.example.android.festemberoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QRScanner_admin extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    TextView myTextView;
    String auth_pin,user_roll,user_hash;
    private QRCodeReaderView mydecoderview;
    private ImageView line_image;
    static boolean QRcoderead_admin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_admin);


        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview_admin);
        mydecoderview.setOnQRCodeReadListener(this);

        myTextView = (TextView) findViewById(R.id.title_QRcode_admin);

        line_image = (ImageView) findViewById(R.id.red_line_image_admin);

        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        line_image.setAnimation(mAnimation);


    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        if(!QRcoderead_admin) {
            Log.d("Admin QR Code read", "Success"+text);
            QRcoderead_admin = true;
            Toast.makeText(getApplicationContext(),"Scanned admin QR",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(QRScanner_admin.this,QRscanner.class);
            intent.putExtra("oc_user_id",text);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }



    private boolean USER_IS_GOING_TO_EXIT=false;
    @Override
    public void onBackPressed() {
        if(USER_IS_GOING_TO_EXIT){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);
        }
        else {
            USER_IS_GOING_TO_EXIT=true;
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}
