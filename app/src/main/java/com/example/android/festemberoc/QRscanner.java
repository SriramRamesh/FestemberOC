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
import com.google.zxing.ChecksumException;

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
    static boolean QRcoderead=false;
    String gender=null;
    boolean authenticated=false;
    String tshirt_size,size,OC_gender,amount;
    boolean tshirt_given,fcard_given,extra_given;
    private static boolean scanned_admin;

    int oc_user_id=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        scanned_admin=false;
        /*Intent intent=getIntent();
        String temp=intent.getStringExtra("oc_user_id");
        validate_oc_user_id(temp);
*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        auth_pin=sharedPreferences.getString("auth_pin", null);
        size=sharedPreferences.getString("Size", null);
        OC_gender=sharedPreferences.getString("OCgender",null);


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
    private void validate_oc_user_id(String s){
        try{
            int i=Integer.parseInt(s);
        }catch (Exception e){
            oc_user_id=-1;
            return;
        }
        oc_user_id=Integer.parseInt(s);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                   wait(1000);
                    QRcoderead=false;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        runnable.run();

        return;

    }


    @Override
    public void onQRCodeRead(String s, PointF[] pointFs){

            Log.d("QR Code read", "Success\n qr:" + s);
            process_QR(s);




    }
    void process_QR(String s){
        if (!QRcoderead) {
            Log.d("Qcode read", "false");

            if (scanned_admin) {

                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Scanning QRCode...");
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                String list[] = s.split("\\$\\$\\$");
                Log.d("test", "List:" + list + "\n String" + s);
                user_roll = list[0];
                user_hash = list[1];
                authenticate();
                Log.d("test", "roll" + user_roll + "\nhash" + user_hash);
            } else {
                Log.d("validate s", "success");
                QRcoderead = true;
                validate_oc_user_id(s);
                Toast.makeText(getApplicationContext(), "Scanned admin QR code", Toast.LENGTH_LONG).show();
                scanned_admin = true;

            }

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
        String api=getString(R.string.apiUrl);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://"+api+"/tshirt/getDetails",
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
                                                //new update_QRboolean().execute();
                                                QRcoderead=false;
                                                pDialog.dismiss();
                                                return;
                                            }
                                        }
                                        else if (!(size.equals(tshirt_size))) {
                                            Toast.makeText(getApplicationContext(), "Wrong tshirt Size\n" + "Actual: " + tshirt_size, Toast.LENGTH_SHORT).show();
                                            //new update_QRboolean().execute();
                                            pDialog.dismiss();
                                            QRcoderead=false;
                                            return;
                                        }
                                    }
                                }
                                if(amount.equals("550")){
                                    pDialog.dismiss();
                                    QRcoderead=false;
                                    //new update_QRboolean().execute();

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
                                if(!(OC_gender.equals(gender))){
                                    Toast.makeText(getApplicationContext(),"Registered gender : "+gender,Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    QRcoderead=false;
                                    //new update_QRboolean().execute();
                                    return;
                                }
                                pDialog.dismiss();
                                QRcoderead=false;
                                //new update_QRboolean().execute();

                                Intent in = new Intent(QRscanner.this,Success.class);
                                in.putExtra("fcard_given",fcard_given);
                                in.putExtra("user_roll",user_roll);
                                in.putExtra("tshirt_given",tshirt_given);
                                in.putExtra("user_hash",user_hash);
                                in.putExtra("gender",gender);
                                in.putExtra("gender",gender);
                                in.putExtra("tshirt_size",tshirt_size);
                                in.putExtra("amount",amount);
                                startActivity(in);

                            }
                            else{
                                String data=jsonResponse.getString("data");
                                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                //new update_QRboolean().execute();
                                QRcoderead=false;
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            QRcoderead=false;
                            //new update_QRboolean().execute();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError data) {
                        pDialog.dismiss();
                        QRcoderead=false;
                        //new update_QRboolean().execute();
                        data.printStackTrace();
                        Toast.makeText(QRscanner.this, "Error Response", Toast.LENGTH_SHORT).show();

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

    public void authenticate(){
        StringRequest admin_postRequest = new StringRequest(Request.Method.POST, "https://"+"admin.pragyan.org"+"/home/oc/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");

                            Log.d("Debug","json: "+jsonResponse);
                            switch (status){
                                case 200:{
                                    authenticated=true;
                                    checkQR();
                                    break;
                                }
                                case 400:{
                                    pDialog.dismiss();
                                    QRcoderead=false;
                                    Toast.makeText(getApplicationContext(),"Invalid request..... please contact pragyan admin",Toast.LENGTH_LONG).show();

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            QRcoderead=false;
                            //new update_QRboolean().execute();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError data) {
                        data.printStackTrace();
                        Toast.makeText(QRscanner.this, "Error Response", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        QRcoderead=false;

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("user_roll",user_roll);
                params.put("oc_user_id",oc_user_id+"");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(admin_postRequest);

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
