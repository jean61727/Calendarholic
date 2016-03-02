package com.example.jeanlee.calendar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileActivity extends ActionBarActivity {

    private final static String STORETEXT="storetext.txt";

    private EditText txtEditor;
    private Button retrieveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        txtEditor=(EditText)findViewById(R.id.textbox);
        retrieveText=(Button)findViewById(R.id.button2);


}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
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
    public void saveClicked(View v) {

        try {

            OutputStreamWriter out=

                    new OutputStreamWriter(openFileOutput(STORETEXT, 0));

            out.write(txtEditor.getText().toString());

            out.close();

            Toast.makeText(this, "The contents are saved in the file.", Toast.LENGTH_LONG).show();

        }

        catch (Throwable t) {

            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();

        }

    }

    static final int READ_BLOCK_SIZE = 100;

    public void ReadBtn(View v) {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput(STORETEXT);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


