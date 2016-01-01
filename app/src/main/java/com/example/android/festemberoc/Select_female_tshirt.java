package com.example.android.festemberoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Select_female_tshirt extends AppCompatActivity {

    Button female_tshirt_Y,female_tshirt_N;
    boolean female_tshirt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_female_tshirt);

        sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        female_tshirt_Y=(Button)findViewById(R.id.ftshirt_yes);
        female_tshirt_N=(Button)findViewById(R.id.ftshirt_no);
        female_tshirt_Y.setOnClickListener(listener);
        female_tshirt_N.setOnClickListener(listener);
    }
    Button.OnClickListener listener=new Button.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.ftshirt_no:{
                    female_tshirt=false;
                    editor.putBoolean("f_tshirt",false);
                    editor.apply();
                    Intent in=new Intent(Select_female_tshirt.this,Select_fCard.class);
                    startActivity(in);
                    break;
                }
                case R.id.ftshirt_yes:{
                    female_tshirt=true;
                    editor.putBoolean("f_tshirt", true);
                    editor.apply();
                    Intent in=new Intent(Select_female_tshirt.this,Select_fCard.class);
                    startActivity(in);
                    break;
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seleact_female_tshirt, menu);
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
