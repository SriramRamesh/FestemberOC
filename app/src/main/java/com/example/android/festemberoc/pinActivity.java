package com.example.android.festemberoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class pinActivity extends AppCompatActivity {

    String PinStr;
    String gender;
    int Pin;
    SharedPreferences sharedPreferences;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pin, menu);
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

    public boolean isNumeric(String S) {
        try {
            int t = Integer.parseInt(S);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
                                Intent in = new Intent(pinActivity.this, QRscanner.class);
                                startActivity(in);

                            }
                            else{
                                String data=jsonResponse.getString("data");
                                Toast.makeText(pinActivity.this, data, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(pinActivity.this, "Error", Toast.LENGTH_SHORT).show();

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
