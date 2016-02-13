package com.example.android.festemberoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Success extends Activity {

    ProgressDialog pDialog;
    Button fcard,tshirt;
    TextView roll;
    Boolean fcard_given,female_tshirt,tshirt_given,giving_fcard;
    String auth_pin,user_roll,user_hash,tshirt_size,size,gender,amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        SharedPreferences sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        auth_pin=sharedPreferences.getString("auth_pin", null);
        giving_fcard=sharedPreferences.getBoolean("fcard", false);
        size = sharedPreferences.getString("Size", null);
        female_tshirt=sharedPreferences.getBoolean("f_tshirt",false);
        fcard=(Button)findViewById(R.id.fcard);
        tshirt=(Button)findViewById(R.id.tshirt);

        Intent in=getIntent();
        tshirt_size=in.getStringExtra("tshirt_size");
        user_hash = in.getStringExtra("user_hash");
        user_roll = in.getStringExtra("user_roll");
        fcard_given=in.getBooleanExtra("fcard_given", false);
        tshirt_given = in.getBooleanExtra("tshirt_given", false);
        gender=in.getStringExtra("gender");
        amount=in.getStringExtra("amount");

        roll=(TextView)findViewById(R.id.Roll);
        if(!giving_fcard){
            fcard.setVisibility(View.INVISIBLE);
            fcard.setClickable(false);
        }
        if(gender.equals("male")) {
               if (size.equals("No")) {
                    tshirt.setVisibility(View.INVISIBLE);
                    tshirt.setClickable(false);
                }

        }
        else{
           if(!female_tshirt){
               tshirt.setVisibility(View.INVISIBLE);
               tshirt.setClickable(false);
           }
        }
        roll.setText("Roll NO: "+user_roll);
        tshirt.setText("TSHIRT :"+tshirt_size);
        if(amount.equals("550")){
            tshirt.setVisibility(View.INVISIBLE);
            tshirt.setClickable(false);
        }
        SetButtonColor();

    }
    public void SetButtonColor(){
        if(fcard_given){
            fcard.setBackgroundColor(Color.RED);
           // fcard.setClickable(false);
        }
        else{

            fcard.setBackgroundColor(Color.GREEN);
        }
        if(tshirt_given){
            tshirt.setBackgroundColor(Color.RED);
            //tshirt.setClickable(false);
        }
        else{
            tshirt.setBackgroundColor(Color.GREEN);
        }
    }
    public void fcardPressed(View view){
        if(fcard_given){
            Toast.makeText(getApplicationContext(),"Already given foodcard",Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating Food card...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        Update("fcard");

    }
    public void Update(final String type){


        JSONObject params = new JSONObject();
        try {

            // the POST parameters:
            params.put("user_roll", user_roll);
            params.put("user_hash", user_hash);
            params.put("auth_pin", auth_pin);
            params.put("type", type);
            Log.d("debug","user_roll: "+user_roll+"user_hash:"+user_hash+"auth_pin :"+auth_pin+"type :"+type+"given :"+1);
            if (type.equals("tshirt")) {
                if (tshirt_given) {
                    params.put("given", 0);

                } else {
                    params.put("given", 1);
                }

            } else if (type.equals("fcard")) {
                if (fcard_given) {
                    params.put("given", 0);
                } else {
                    params.put("given", 1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String api=getString(R.string.apiUrl);
        JsonObjectRequest postRequest = new JsonObjectRequest("https://"+api+"/tshirt/update",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonResponse) {

                        try {
                            int status = jsonResponse.getInt("status");

                            if(status==2){
                                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                                if(type.equals("tshirt")){
                                    if(!tshirt_given) {
                                        tshirt_given = true;
                                    }
                                }
                                if(type.equals("fcard")){
                                    if(!fcard_given) {
                                        fcard_given = true;
                                    }
                                }
                                SetButtonColor();
                                pDialog.dismiss();
                                return;
                            }
                            else if(status==3){
                                String data=jsonResponse.getString("data");
                                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                return;
                            }
                            else if(status==0){
                                Toast.makeText(getApplicationContext(),"User not registered",Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                return;
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Incorrect credentials",Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
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
                        data.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }
        ) {

        };
        Volley.newRequestQueue(this).add(postRequest);


    }
    public void tshirtPressed(View view){
        if(tshirt_given){
            Toast.makeText(getApplicationContext(),"Already given tshirt",Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating tshirt...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        Update("tshirt");

    }

    @Override
    public void onPause(){
        super.onPause();
        SetButtonColor();

    }
    @Override
    public void onResume(){
        super.onResume();
        SetButtonColor();

    }

    public void Next_Scan(View view){
        finish();
        Intent intent=new Intent(this,QRscanner.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_success, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
