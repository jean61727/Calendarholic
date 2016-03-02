package com.example.jeanlee.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import sqlite.helper.AlbumDBhelper;
import sqlite.model.Album;


public class AlbumInputActivity extends ActionBarActivity {
    /*   static final int REQUEST_IMAGE_CAPTURE = 1;
       private ImageView mImageView;*/
    private AlbumDBhelper db;
    /**request Code 从相册选择照片并裁切**/
    private final static int SELECT_PIC=123;
    /**request Code 从相册选择照片不裁切**/
    private final static int SELECT_ORIGINAL_PIC=126;
    /**request Code 拍取照片并裁切**/
    private final static int TAKE_PIC=124;
    /**request Code 拍取照片不裁切**/
    private final static int TAKE_ORIGINAL_PIC=127;
    /**request Code 裁切照片**/
    private final static int CROP_PIC=125;
    private Uri imageUri;
    private ImageView imgShow;
    private EditText title;
    private EditText descrip;
    private static  EditText mDate;
    private Button save;
    private Button cancel;
    private byte[] photo_crop;


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            view.updateDate(year, month, day);
            month++;
            mDate.setText(year+"/"+month+"/"+day);

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_input);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        db=AlbumDBhelper.getInstance(this);
        imageUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg"));

        imgShow=(ImageView)findViewById(R.id.imgtaken);
        title = (EditText)findViewById(R.id.album_title);
        descrip = (EditText)findViewById(R.id.album_descri);
        save = (Button)findViewById(R.id.album_save);
        cancel = (Button)findViewById(R.id.album_cancel);
        mDate = (EditText)findViewById(R.id.album_datetext);
        mDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addAlbumNow(title, descrip, mDate);
                if (TextUtils.isEmpty(title.getText().toString())) {
                  //  makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }



        });
    }
    public void addAlbumNow(EditText title,EditText descrip,EditText mDate) {
        String sTitle = title.getText().toString();
        String sDescrip= descrip.getText().toString();
        String sDate=mDate.getText().toString();
        if (sTitle.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the task description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            Album album=new Album(sTitle,sDescrip,sDate);
            album.setPhoto(photo_crop);
            //get the id of the created journal
            long listid=db.createAlbum(album);
            album.setId(listid);

            title.setText("");
            descrip.setText("");
//            adapt.add(task);
//            adapt.notifyDataSetChanged();
            Intent intent = new Intent(this, AlbumActivity.class);
            setResult(100, intent);
            finish();
        }

    }
//    private void makeToast() {
//        Toast.makeText(AlbumInputActivity.this, "Please maintain a summary",
//                Toast.LENGTH_LONG).show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_input, menu);
        //set background


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.btnCropFromGallery://从相册选择照片进行裁剪
                cropFromGallery();
                break;
            case R.id.take_photo://从相机拍取照片进行裁剪
                cropFromTake();
                break;
           /* case R.id.btnOriginal://从相册选择照片不裁切
                selectFromGallery();
                break;
            case R.id.btnTakeOriginal://从相机拍取照片不裁剪
                selectFromTake();
                break;*/
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SELECT_PIC:
                if (resultCode==RESULT_OK) {//从相册选择照片并裁切
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imgShow.setImageBitmap(bitmap);
                        ByteArrayOutputStream out =new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                        photo_crop = out.toByteArray();
                        imgShow.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                        //}

                        //bitmap.recycle();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_ORIGINAL_PIC:
                if (resultCode==RESULT_OK) {//从相册选择照片不裁切
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ByteArrayOutputStream out =new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

                        ContentValues cv=new ContentValues();
                        Album photo=new Album();
                        photo.setPhoto(out.toByteArray());
                        photo.setTitle("picture");
                        long id=db.createAlbum(photo);
                        Album get=db.getAlbum(id);
                        Log.e("album",get.getTitle());
                        //cv.put("picture",out.toByteArray());

                        imgShow.setImageBitmap(bitmap);
                        bitmap.recycle();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PIC://拍取照片,并裁切
                if (resultCode==RESULT_OK) {
                    cropImageUri(imageUri, 600, 600, CROP_PIC);
                }
            case TAKE_ORIGINAL_PIC://拍取照片
                if (resultCode==RESULT_OK) {
                    String imgPath=imageUri.getPath();//获取拍摄照片路径
                }
                break;
            case CROP_PIC://拍取照片
                if (resultCode==RESULT_OK) {
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//将imageUri对象的图片加载到内存
                        imgShow.setImageBitmap(bitmap);
                        
                        ByteArrayOutputStream out =new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                        photo_crop = out.toByteArray();
                        imgShow.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪指定uri对应的照片
     * @param imageUri：uri对应的照片
     * @param outputX：裁剪宽
     * @param outputY：裁剪高
     * @param requestCode：请求码
     */
    private void cropImageUri(Uri imageUri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }



    /**
     * 从相册选择原生的照片（不裁切）
     */
    private void selectFromGallery() {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        startActivityForResult(intent, SELECT_ORIGINAL_PIC);
    }
    /**
     * 从相册选择照片进行裁剪
     */
    private void cropFromGallery() {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        intent.putExtra("crop", "true");//设置为裁切
        intent.putExtra("aspectX", 1);//裁切的宽比例
        intent.putExtra("aspectY", 1);//裁切的高比例
        intent.putExtra("outputX", 600);//裁切的宽度
        intent.putExtra("outputY", 600);//裁切的高度
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将裁切的结果输出到指定的Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, SELECT_PIC);
    }
    /**
     * 拍取照片不裁切
     */
    private void selectFromTake() {

        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, TAKE_ORIGINAL_PIC);
    }
    /**
     * 从相机拍取照片进行裁剪
     */
    private void cropFromTake() {

        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, TAKE_PIC);
    }

}
