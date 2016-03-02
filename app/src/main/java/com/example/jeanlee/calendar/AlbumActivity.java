package com.example.jeanlee.calendar;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.AlbumDBhelper;
import sqlite.model.Album;


public class AlbumActivity extends ActionBarActivity {


    AlbumDBhelper db;
    List<Album> albums ;
    MyAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albm);
        //set background
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        db = AlbumDBhelper.getInstance(this);
        albums = db.getAllAlbums();
        adapt = new MyAdapter(this, R.layout.list_inner_view, albums);
        GridView listAlbum = (GridView) findViewById(R.id.gridView2);
        listAlbum.setAdapter(adapt);

        listAlbum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Album album=albums.get(index);
                db.deleteAlbum(album.getId());
                albums.remove(index);

                adapt.notifyDataSetChanged();
                adapt.notifyDataSetInvalidated();
                return true;
            }
        });

        listAlbum.setClickable(true);
        listAlbum.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(AlbumActivity.this, ChangeAlbum.class);
                Album album=albums.get(position);
                intent.putExtra("id",album.getId());
                Log.e("id!!!!!",""+album.getId());
                startActivityForResult(intent , 100);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.photo_add_item:
                // Hide the "empty" view since there is now at least one item in the list.
                Intent intent = new Intent(this,AlbumInputActivity.class);
                startActivityForResult(intent, 100);
                return true;
        }



        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    private class MyAdapter extends ArrayAdapter<Album> {

        Context context;
        List<Album> albumlist = new ArrayList<Album>();
        int layoutResourceId;
        TextView date ;
        TextView title;
        TextView descrip;
        ImageView img;
        public int[] image = new int[]{R.drawable.photo_1,R.drawable.photo_2,R.drawable.photo_3};
        public MyAdapter(Context context, int layoutResourceId,
                         List<Album> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.albumlist = objects;
            this.context = context;
        }

        /**
         * This method will DEFINe what the view inside the list view will
         * finally look like Here we are going to code that the checkbox state
         * is the status of task and check box text is the task name
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Album album= getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
               convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_items, parent, false);
          /*      convertView = LayoutInflater.from(context).inflate(R.layout.album_items, null);

                AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                        150,
                        200);
                convertView.setLayoutParams(param);*/
            }
            // Lookup view for data population

            date = (TextView) convertView.findViewById(R.id.album_item_date);
            title = (TextView) convertView.findViewById(R.id.album_item_title);
            descrip=(TextView)convertView.findViewById(R.id.album_item_descri);
            img = (ImageView)convertView.findViewById(R.id.album_item_img);
            if(albumlist.size()>1){
               // LinearLayout layout = (LinearLayout)findViewById(R.id.album_item_layout);
                int t = position%3;
                //convertView.setBackgroundResource(image[t]);
             //   layout.setBackgroundResource(image[t]);
            }
            byte[] b = album.getPhoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length,null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            Drawable drawable = bitmapDrawable;
            // Populate the data into the template view using the data object
            date.setText(album.getDateAt());
            descrip.setText(album.getDescrip());
            title.setText(album.getTitle());
            img.setImageDrawable(drawable);
            img.setVisibility(View.VISIBLE);

            // Return the completed view to render on screen
           /* Animation animation = null;
            animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0,
                    (float) 1.0);

            animation.setDuration(750);
            convertView.startAnimation(animation);
            animation = null;*/



            return convertView;
        }

    }
}
