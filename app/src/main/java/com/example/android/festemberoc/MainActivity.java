package com.example.android.festemberoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button mB,fB;

    Button.OnClickListener genderListener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.male:{
                    editor.putString("OCgender","male");
                    editor.apply();
                    Intent in= new Intent(MainActivity.this,Select_male_tshirt.class);
                    startActivity(in);
                    break;
                }
                case R.id.female:{
                    editor.putString("OCgender","female");
                    editor.apply();
                    Intent in =new Intent(MainActivity.this,Select_female_tshirt.class);
                    startActivity(in);
                    break;
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mB=(Button)findViewById(R.id.male);
        mB.setOnClickListener(genderListener);
        fB=(Button)findViewById(R.id.female);
        fB.setOnClickListener(genderListener);
    }


}
