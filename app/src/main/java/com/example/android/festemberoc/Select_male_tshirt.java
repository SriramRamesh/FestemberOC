package com.example.android.festemberoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Select_male_tshirt extends Activity {

    SharedPreferences sharedPreferences;
    Button tS,tM,tL,tXL,tN;
    String tshirt_size;
    SharedPreferences.Editor editor;

    private void Select_fCard(){
        Intent in=new Intent(Select_male_tshirt.this,Select_fCard.class);
        if(tshirt_size.equals("No")){
            editor.putBoolean("m_tshirt", false);
        }
        else{
            editor.putBoolean("m_tshirt",true);
        }
        editor.putString("Size",tshirt_size);
        editor.apply();
        startActivity(in);
    }


    Button.OnClickListener listener=new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.small: {
                    tshirt_size = "S";
                    Select_fCard();


                    break;
                }
                case R.id.medium: {
                    tshirt_size = "M";
                    Select_fCard();
                    break;
                }
                case R.id.large: {
                    tshirt_size = "L";
                    Select_fCard();
                    break;
                }
                case R.id.XLarge: {
                    tshirt_size = "XL";
                    Select_fCard();
                    break;
                }
                case R.id.tshirt_No: {
                    tshirt_size = "No";
                    Select_fCard();
                    break;
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_male_tshirt);

        sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

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



        tN=(Button)findViewById(R.id.tshirt_No);
        tN.setOnClickListener(listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_male_tshirt, menu);
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
