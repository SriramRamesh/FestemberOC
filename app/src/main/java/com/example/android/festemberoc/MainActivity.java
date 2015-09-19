package com.example.android.festemberoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {
    int Pin;
    String PinStr=null;
    boolean inc_pin=true;
    String tshirt_size;
    boolean fcard;
    boolean Is_fcard_filled=false;
    String layout=null;
    Button tS,tM,tL,tXL,tXXL,tN,fY,fN;
    SharedPreferences sharedPreferences;

    private Button.OnClickListener listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch(v.getId()){
                case R.id.small:{
                    tshirt_size="S";
                    Select_fCard();


                    break;
                }
                case R.id.medium:{
                    tshirt_size="M";
                    Select_fCard();
                    break;
                }
                case R.id.large:{
                    tshirt_size="L";
                    Select_fCard();
                    break;
                }
                case R.id.XLarge:{
                    tshirt_size="XL";
                    Select_fCard();
                    break;
                }
                case R.id.XXLarge:{
                    tshirt_size="XXL";
                    Select_fCard();
                    break;
                }
                case R.id.tshirt_No:{
                    tshirt_size="No";
                    Select_fCard();
                    break;
                }
                case R.id.fcard_yes:{
                    fcard=true;
                    Is_fcard_filled=true;
                    break;
                }
                case R.id.fcard_no:{
                    Is_fcard_filled=true;
                    fcard=false;
                    break;
                }
                default:{
                    Log.d("Debug","invalid button type ");
                }

            }
            if(Is_fcard_filled) {
                if(gender.equals("male")) {
                    if ((tshirt_size.equals("No")) && !fcard && layout.equals("fcard") ) {
                        Toast.makeText(getApplicationContext(), "Choose tshirt or fcard ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                editor.putString("Size", tshirt_size);
                editor.putBoolean("Food", fcard);
                editor.apply();
                setContentView(R.layout.pin_view_layout);
                layout="pin";
            }
        }
    };

    Button mB,fB;
    String gender=null;
    Button.OnClickListener genderListener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.male:{
                    gender="male";
                    Select_tshirt();
                    break;
                }
                case R.id.female:{
                    gender="female";
                    Select_fCard();
                    break;
                }
            }

        }
    };
    private void Select_tshirt(){

        setContentView(R.layout.tshirt);
        layout="tshirt";
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        tS=(Button)findViewById(R.id.small);
        tS.setOnClickListener(listener);

        tM=(Button)findViewById(R.id.medium);
        tM.setOnClickListener(listener);

        tL=(Button)findViewById(R.id.large);
        tL.setOnClickListener(listener);

        tXL=(Button)findViewById(R.id.XLarge);
        tXL.setOnClickListener(listener);

        tXXL=(Button)findViewById(R.id.XXLarge);
        tXXL.setOnClickListener(listener);

        tN=(Button)findViewById(R.id.tshirt_No);
        tN.setOnClickListener(listener);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Select_gender();

    }
    private void Select_gender(){
        setContentView(R.layout.activity_main);
        layout="main";

        mB=(Button)findViewById(R.id.male);
        mB.setOnClickListener(genderListener);
        fB=(Button)findViewById(R.id.female);
        fB.setOnClickListener(genderListener);
    }
    @Override
    public void onBackPressed(){

        if(layout.equals("fcard")) {
            Select_tshirt();
        }
        else if(layout.equals("pin")) {
            Select_fCard();
        }
        else if (layout.equals("tshirt")){
            Select_gender();
        }
        else
        {
            super.onBackPressed();
        }

    }

    private void Select_fCard(){
        setContentView(R.layout.fcard);
        layout = "fcard";
        fY = (Button) findViewById(R.id.fcard_yes);
        fY.setOnClickListener(listener);

        fN = (Button) findViewById(R.id.fcard_no);
        fN.setOnClickListener(listener);
    }

    public boolean isNumeric(String S) {
        try {
            int t = Integer.parseInt(S);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void Continue(View view) {
        Button continue_button=(Button)findViewById(view.getId());
        continue_button.setClickable(false);

        EditText pin = (EditText) findViewById(R.id.Pin);
        PinStr = pin.getText().toString();
        if (("".equals(PinStr)) || (!isNumeric(PinStr))) {
            Toast.makeText(getApplicationContext(), "Empty or improper pin", Toast.LENGTH_SHORT).show();
            continue_button.setClickable(true);
            return;
        }
        Pin = Integer.parseInt(PinStr);
        check_login();
        //TODO: Make an api call and verify the pin

        Log.d("Debug", "pin=" + Pin);

        continue_button.setClickable(true);
    }

    public void check_login(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Authenticating...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://api.festember.com/oc/tshirt/auth",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            pDialog.dismiss();

                            if(status==1){
                                Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                                sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("auth_pin", PinStr);
                                editor.putString("OC_gender",gender);
                                editor.apply();
                                Intent in = new Intent(MainActivity.this, QRscanner.class);
                                startActivity(in);

                            }
                            else{
                                String data=jsonResponse.getString("data");
                                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                                return;

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError data) {
                        pDialog.dismiss();
                        data.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("auth_pin",PinStr);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
}
