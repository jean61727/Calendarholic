package com.example.jeanlee.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import sqlite.helper.TodoDatabaseHelper;


public class PetActivity extends ActionBarActivity {

    private TodoDatabaseHelper db;
    private int taskCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        db=TodoDatabaseHelper.getInstance(this);
        taskCount =db.getTaskCount();
        Log.e("taskcount",""+taskCount);
        //
        GridView gridview = (GridView)findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(PetActivity.this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ImageAdapter(Context c) {
            mContext = c;
        }
        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return mThumbIds[position];
        }
        public long getItemId(int position) {
            return mThumbIds[position];
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setPadding(0, 0, 0, 0);

            } else {
                imageView = (ImageView) convertView;
            }

            if(position==0 && taskCount>=5){
                imageView.setBackgroundColor(Color.argb(150, 255, 255, 255));
                imageView.setImageResource(mThumbIds[position]);
            }

            if(position==1 && taskCount>=10){
                imageView.setImageResource(mThumbIds[position]);
                imageView.setBackgroundColor(Color.argb(150, 255, 255, 255));

            }
            if(position==2 && taskCount>=15){
                imageView.setImageResource(mThumbIds[position]);
                imageView.setBackgroundColor(Color.argb(150, 255, 255, 255));

            }

//            Bitmap imageBitmap =decodeSampledBitmapFromResource(getResources(),mThumbIds[position],
//                    imageView.getMeasuredWidth(),imageView.getMeasuredHeight());
//            Log.e("imageView" ,"image"+mThumbIds[position]+"is"+imageView.getMeasuredWidth());
//            AsyncBitmapDecoder.asyncDecodeImage(getResources(), imageView, mThumbIds[position],
//                   imageView.getMeasuredWidth(), 0);

           // imageView.setImageBitmap(imageBitmap);
            return imageView;
        }
        private Integer[] mThumbIds = {
                R.drawable.monster_1,R.drawable.monster_2,R.drawable.monster_3
        };
        public View getView1(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pet, menu);
        return true;
    }
    private Bitmap downscaleBitmapUsingDensities(final int sampleSize,final int imageResId)
    {
        final BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inDensity=sampleSize;
        bitmapOptions.inTargetDensity=1;
        final Bitmap scaledBitmap=BitmapFactory.decodeResource(getResources(),imageResId,bitmapOptions);
        scaledBitmap.setDensity(Bitmap.DENSITY_NONE);
        return scaledBitmap;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // Test image size without decoding it.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate down-sample rate and decode the image using it.
        // Note that 'inSampleSize' must be a number that is a power of 2,
        // other values will be rounded to the nearest number that is a power of 2.
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {

        int size=Math.min(reqWidth == 0 ? Integer.MAX_VALUE : options.outWidth / reqWidth,
                reqHeight == 0 ? Integer.MAX_VALUE : options.outHeight / reqHeight);
        Log.e("size","size"+size);
        return size;
    }




}
