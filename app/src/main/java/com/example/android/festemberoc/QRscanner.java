package com.example.android.festemberoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class QRscanner extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    TextView myTextView;
    String auth_pin,user_roll,user_hash;
    private QRCodeReaderView mydecoderview;
    private ImageView line_image;
    boolean QRcoderead=false;
    String gender=null;
    String tshirt_size,size,OC_gender,amount;
    boolean tshirt_given,fcard_given,extra_given;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        auth_pin=sharedPreferences.getString("auth_pin", null);
        size=sharedPreferences.getString("Size", null);
        OC_gender=sharedPreferences.getString("OC_gender",null);


        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);

        myTextView = (TextView) findViewById(R.id.exampleTextView);

        line_image = (ImageView) findViewById(R.id.red_line_image);

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
    public void onQRCodeRead(String s, PointF[] pointFs) {


        if(!QRcoderead) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Scanning QRCode...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            QRcoderead = true;
            String list[] = s.split("&");
            String temp[] = list[1].split("=");
            user_roll = temp[1];
            temp = list[2].split("=");
            user_hash = temp[1];
            Log.d("debug", "roll" + user_roll + "\nhash" + user_hash);
            //TODO: Make an API Call...
            checkQR();
        }

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

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
    public void checkQR(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://api.festember.com/oc/tshirt/getDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");

                            Log.d("Debug","json: "+jsonResponse);

                            if(status==2){
                                JSONObject data=jsonResponse.getJSONObject("data");
                                tshirt_size=data.getString("tshirt_size");
                                tshirt_given=data.getBoolean("tshirt_given");
                                fcard_given=data.getBoolean("fcard_given");
                                extra_given=data.getBoolean("extra_given");
                                gender=data.getString("gender");
                                amount=data.getString ("amount");
                                //TODO: Change the shirt sizes
                                if(OC_gender.equals("male")){
                                    if(!(size.equals("No"))) {
                                        if(size.equals("S")){
                                            if(!(tshirt_size.equals("S")||tshirt_size.equals("XXL"))){
                                                Toast.makeText(getApplicationContext(), "Wrong tshirt Size\n" + "Actual: " + tshirt_size, Toast.LENGTH_SHORT).show();
                                                new update_QRboolean().execute();
                                                pDialog.dismiss();
                                                return;
                                            }
                                        }
                                        else if (!(size.equals(tshirt_size))) {
                                            Toast.makeText(getApplicationContext(), "Wrong tshirt Size\n" + "Actual: " + tshirt_size, Toast.LENGTH_SHORT).show();
                                            new update_QRboolean().execute();
                                            pDialog.dismiss();
                                            return;
                                        }
                                    }
                                }
                                if(!(OC_gender.equals(gender))){
                                    Toast.makeText(getApplicationContext(),"Registered gender : "+gender,Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    new update_QRboolean().execute();
                                    return;
                                }
                                pDialog.dismiss();
                                new update_QRboolean().execute();

                                Intent in = new Intent(QRscanner.this,Success.class);
                                in.putExtra("fcard_given",fcard_given);
                                in.putExtra("user_roll",user_roll);
                                in.putExtra("tshirt_given",tshirt_given);
                                in.putExtra("user_hash",user_hash);
                                in.putExtra("gender",gender);
                                in.putExtra("tshirt_size",tshirt_size);
                                in.putExtra("amount",amount);
                                startActivity(in);

                            }
                            else{
                                String data=jsonResponse.getString("data");
                                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                new update_QRboolean().execute();

                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            new update_QRboolean().execute();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError data) {
                        pDialog.dismiss();
                        new update_QRboolean().execute();
                        data.printStackTrace();
                        Toast.makeText(QRscanner.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("user_roll",user_roll);
                params.put("user_hash",user_hash);
                params.put("auth_pin",auth_pin);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
    private class update_QRboolean extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                wait(1000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void params){
            QRcoderead=false;
            return ;
        }
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
