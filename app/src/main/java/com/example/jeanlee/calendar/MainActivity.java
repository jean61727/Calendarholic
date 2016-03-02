package com.example.jeanlee.calendar;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends ActionBarActivity{


    ImageView calendarButton;
    ImageView journalButton;
    ImageView wishButton;
    ImageView albumButton;
    ImageView todoButton;
    ImageView filedemoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        todoButton=(ImageView)findViewById(R.id.btLeft1);
        todoButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), TodoActivity.class);
                startActivity(intent);
            }

        });


        albumButton=(ImageView)findViewById(R.id.btLeft2);
        albumButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), AlbumActivity.class);
                startActivity(intent);
            }

        });


        journalButton=(ImageView)findViewById(R.id.btRight1);
        journalButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), JournalActivity.class);
                startActivity(intent);
            }

        });

        wishButton=(ImageView)findViewById(R.id.btRight2);
        wishButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), WishListActivity.class);
                startActivity(intent);
            }

        });

        calendarButton=(ImageView)findViewById(R.id.btLeft3);
        calendarButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CalendarViewActivity.class);
                startActivity(intent);
            }

        });

        filedemoButton=(ImageView)findViewById(R.id.btRight3);
        filedemoButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),PetActivity.class);
                startActivity(intent);
            }

        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
