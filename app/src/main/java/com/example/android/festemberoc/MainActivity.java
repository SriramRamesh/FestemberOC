package com.example.android.festemberoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    String tshirt_size;
    boolean fcard;
    boolean Is_fcard_filled=false;
    Button tS,tM,tL,tXL,tXXL,tN,fY,fN;
    SharedPreferences sharedPreferences;

    private static final String API_URL = "http://freemusicarchive.org/api";
    private Button.OnClickListener listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch(v.getId()){
                case R.id.small:{
                    tshirt_size="S";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);



                    break;
                }
                case R.id.medium:{
                    tshirt_size="M";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

                    break;
                }
                case R.id.large:{
                    tshirt_size="L";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

                    break;
                }
                case R.id.XLarge:{
                    tshirt_size="XL";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

                    break;
                }
                case R.id.XXLarge:{
                    tshirt_size="XXL";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

                    break;
                }
                case R.id.tshirt_No:{
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

                    tshirt_size="No";
                    setContentView(R.layout.fcard);
                    fY=(Button)findViewById(R.id.fcard_yes);
                    fY.setOnClickListener(listener);

                    fN=(Button)findViewById(R.id.fcard_no);
                    fN.setOnClickListener(listener);

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
                editor.putString("Size", tshirt_size);
                editor.putBoolean("Food", fcard);
                editor.apply();
                setContentView(R.layout.pin_view_layout);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tshirt);

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


    /*public void Apply(View view) {
        sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
        RadioGroup TShirt, FoodCoupon;
        TShirt = (RadioGroup) findViewById(R.id.TShirt);
        FoodCoupon = (RadioGroup) findViewById(R.id.FoodCoupon);
        RadioButton Coupon = null, Size = null;
        switch (TShirt.getCheckedRadioButtonId()) {
            case R.id.radioButton: {
                Size = (RadioButton) findViewById(R.id.radioButton);
                break;
            }
            case R.id.radioButton2: {
                Size = (RadioButton) findViewById(R.id.radioButton2);
                break;
            }
            case R.id.radioButton3: {
                Size = (RadioButton) findViewById(R.id.radioButton3);
                break;
            }
            case R.id.radioButton6: {
                Size = (RadioButton) findViewById(R.id.radioButton6);
                break;
            }
            case R.id.radioButton7: {
                Size = (RadioButton) findViewById(R.id.radioButton7);
                break;
            }
            case R.id.radioButton8: {
                Size = (RadioButton) findViewById(R.id.radioButton8);
                break;
            }
            default: {
                Toast.makeText(getApplicationContext(), "Choose both to proceed", Toast.LENGTH_LONG).show();
                return;
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Size", Size.getText().toString());

        switch (FoodCoupon.getCheckedRadioButtonId()) {
            case R.id.radioButton4: {
                Coupon = (RadioButton) findViewById(R.id.radioButton4);
                break;
            }
            case R.id.radioButton5: {
                Coupon = (RadioButton) findViewById(R.id.radioButton5);
                break;
            }
            default: {
                Toast.makeText(getApplicationContext(), "Choose both the raadio groups to proceed", Toast.LENGTH_LONG).show();
                return ;
            }
        }
        editor.putString("Food", Coupon.getText().toString());
        editor.apply();
        setContentView(R.layout.pin_view_layout);

    }
*/
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
        EditText pin = (EditText) findViewById(R.id.Pin);
        PinStr = pin.getText().toString();
        if (("".equals(PinStr)) || (!isNumeric(PinStr))) {
            Toast.makeText(getApplicationContext(), "Empty or improper pin", Toast.LENGTH_LONG).show();
            return;
        }
        Pin = Integer.parseInt(PinStr);

        check_login();
        //TODO: Make an api call and verify the pin
        Log.d("Debug", "pin=" + Pin);



    }
    public void check_login(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Authenticating...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };
        runnable.run();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://api.festember.com/oc/tshirt/auth",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            pDialog.dismiss();

                            if(status==1){
                                Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_LONG).show();
                                sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("auth_pin", PinStr);
                                editor.apply();
                                Intent in = new Intent(MainActivity.this, QRscanner.class);
                                startActivity(in);

                            }
                            else{
                                Toast.makeText(MainActivity.this,"Incorrect Pin",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("auth_pin",Integer.toString(Pin));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
}
