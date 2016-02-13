package com.example.android.festemberoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Select_fCard extends AppCompatActivity {

    boolean fcard;
    Button fY,fN;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent in;

    Button.OnClickListener listener=new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fcard_yes:{
                    fcard=true;
                    editor.putBoolean("fcard",true);
                    editor.apply();
                    startActivity(in);
                    break;
                }
                case R.id.fcard_no:{
                    fcard=false;
                    editor.putBoolean("fcard",false);
                    editor.apply();
                    startActivity(in);
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_fcard);

        sharedPreferences=getSharedPreferences("User Details", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        in=new Intent(Select_fCard.this,pinActivity.class);

        fY = (Button) findViewById(R.id.fcard_yes);
        fY.setOnClickListener(listener);

        fN = (Button) findViewById(R.id.fcard_no);
        fN.setOnClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_f_card, menu);
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
