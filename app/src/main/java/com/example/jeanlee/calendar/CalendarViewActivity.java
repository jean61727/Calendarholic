package com.example.jeanlee.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sqlite.helper.AlbumDBhelper;
import sqlite.helper.CalendarAllDBhelper;
import sqlite.helper.CalendarDBhelper;
import sqlite.helper.TodoDatabaseHelper;
import sqlite.model.Album;
import sqlite.model.Journal;
import sqlite.model.Task;


public class CalendarViewActivity extends Activity {

    private  String daynow;
    public CalendarDBhelper db;
    public TodoDatabaseHelper db2;
    public CalendarAllDBhelper db3;
    public AlbumDBhelper db4;
    private String prefix;
    public Calendar month;
    public ListView list ;
    private ImageView todo;
    private PopupWindow popupWindow;
    private EditText dotimepicker1;
    private EditText dotimepicker2;
    private EditText dowrite;
    private ImageView plus;
    private TextView onclickdate;
    private TextView olddate;
    private View view;
    public int[] image = new int[]{R.drawable.happy , R.drawable.love_2 , R.drawable.meeting , R.drawable.deadline,
            R.drawable.pencil};

    public List<Map<String, Object>> listview_list ;
    //public CalendarAdapter adapter;
    public int today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        month = Calendar.getInstance();
        today = month.get(month.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String input = df.format(month.getTime());
        onNewIntent(input);

        int t = month.get(Calendar.MONTH)+1;
        prefix = month.get(Calendar.YEAR)+"/"+t+"/";
        listview_list = new ArrayList<Map<String, Object>>();
        db = CalendarDBhelper.getInstance(this);
        db2=TodoDatabaseHelper.getInstance(this);
        db3=CalendarAllDBhelper.getInstance(this);
        db4=AlbumDBhelper.getInstance(this);
        list = (ListView)findViewById(R.id.dailyView1);
        plus = (ImageView)findViewById(R.id.calendar_todo);

        CalendarAdapter adapter = new CalendarAdapter(this, month);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        TextView title  = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        ImageView previous  = (ImageView) findViewById(R.id.btn_prev_month);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),today);
                } else {
                    month.set(month.get(Calendar.YEAR),month.get(Calendar.MONTH)-1,today);
                }
                listview_list.clear();
                plus.setVisibility(View.INVISIBLE);

                refreshCalendar();
            }
        });

        ImageView next  = (ImageView) findViewById(R.id.btn_next_month);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),today);
                } else {

                    month.set((month.get(Calendar.YEAR)),month.get(Calendar.MONTH)+1,today);
                }
                plus.setVisibility(View.INVISIBLE);
                listview_list.clear();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onclickdate = (TextView)v.findViewById(R.id.day);

                if(olddate!=null ){
                    if(olddate.getText().toString().equals(Integer.toString(today))){
                        olddate.setBackgroundResource(R.drawable.blue_nowday);
                    }
                    else olddate.setBackgroundResource(R.drawable.pink_calendar2);
                }
                onclickdate.setBackgroundColor(Color.argb(150,204,255,204));
                olddate = onclickdate;

               // v.setBackgroundResource(R.drawable.onclick_calendarback);
                plus.setVisibility(View.VISIBLE);
                getList(onclickdate.getText().toString());

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                                         int index, long arg3) {
                HashMap<String, Object> map=(HashMap)listview_list.get(index);
                long id = (long)map.get("id");
                Log.e("map",""+id);

                db3.deleteCalendar(id);

                listview_list.remove(index);
                month.set((month.get(Calendar.YEAR)),month.get(Calendar.MONTH),today);
                refreshCalendar();

                return true;
            }
        });



        todo = (ImageView)findViewById(R.id.calendar_todo);
        todo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showWindow(v);


            }
        });

    }
    public void getList(String day){
        daynow = "";
        daynow = prefix+ day;

        resetList();
        listview_list.clear();

        List<Journal> journallist = db.getJournalByDate(daynow);
        List<Task> tasklist = db2.getTasksByDate(daynow);
        List<sqlite.model.Calendar> calendars = db3.getCalendarsByDate(daynow);
        List<Album> albums =db4.getAlbumByDate(daynow);

        if(calendars.size()!=0){
            for(sqlite.model.Calendar cal :calendars){
                String title = cal.getTaskName();
                String info = cal.getTime();
                long id=cal.getId();
                Log.e("getid",""+id);
                Map map = new HashMap<String, Object>();
                map.put("title", title);
                map.put("info", info);
                map.put("listview_icon",image[4]);
                map.put("id",id);
                listview_list.add(map);
            }
        }

        if(journallist.size() != 0){
            for(Journal journal : journallist){
                String title = journal.getTitle();
                String info = journal.getDescrip();
                long id=journal.getId();
                Map map = new HashMap<String, Object>();
                map.put("title", title);
                map.put("info", info);
                map.put("listview_icon",image[0]);
                map.put("id",id);
                listview_list.add(map);
            }
        }
        if(tasklist.size() != 0){
            for(Task task : tasklist ){
                String title = task.getTaskName();
                String info;
                long id=task.getId();
                if(task.getStatus()==1) info = "finish";
                else info = "have to do";

                Map map = new HashMap<String, Object>();
                map.put("title", title);
                map.put("info", info);
                map.put("listview_icon",image[3]);
                map.put("id",id);
                listview_list.add(map);
            }
        }
        if(albums.size()!=0){
            for(Album album: albums){
                String title = album.getTitle();
                String info = album.getDescrip();
                long id=album.getId();
                Map map = new HashMap<String, Object>();
                map.put("title", title);
                map.put("info", info);
                map.put("listview_icon",image[1]);
                map.put("id",id);
                listview_list.add(map);
            }
        }

        SimpleAdapter listadapter = new SimpleAdapter(CalendarViewActivity.this, listview_list, R.layout.listview_item,
                new String[]{"title", "info", "listview_icon","id"},
                new int[]{R.id.title, R.id.info, R.id.listview_icon,R.id.id}
        );
            list.setAdapter(listadapter);

    }
  /*  private List<? extends Map<String, ?>> getData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map =
                new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "紅豆");
        map.put("listview_icon", R.drawable.happy);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "綠豆");
        map.put("listview_icon", R.drawable.pencil);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "土豆");
        map.put("listview_icon", R.drawable.deadline);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G4");
        map.put("info", "毛豆");
        map.put("listview_icon", R.drawable.medicine);
        list.add(map);

        return list;
    }*/
    public void resetList(){
        listview_list = new ArrayList<Map<String, Object>>();
    }

    private void getData(String title ,String time ,long id) {

        Map<String, Object> map =  new HashMap<String, Object>();
        if(listview_list==null) resetList();

        map.put("title",title);
        map.put("info",time);
        map.put("listview_icon",image[4]);
        map.put("id",id);
        listview_list.add(map);

    }

    public void refreshCalendar()
    {

        TextView title  = (TextView) findViewById(R.id.title);

        CalendarAdapter adapter = new CalendarAdapter(this, month );

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        //adapter.refreshDays();
        //adapter.notifyDataSetChanged();
        int t = month.get(Calendar.MONTH)+1;
        prefix = "";
        prefix = month.get(Calendar.YEAR)+"/"+t+"/";

        SimpleAdapter listadapter = new SimpleAdapter(CalendarViewActivity.this, listview_list, R.layout.listview_item,
                new String[]{"title", "info", "listview_icon","id"},
                new int[]{R.id.title, R.id.info, R.id.listview_icon,R.id.id}
        );
        list.setAdapter(listadapter);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));


    }

    public void onNewIntent(String nowaday) {
        String date = nowaday;
        String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
        month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1 , Integer.parseInt(dateArr[2]));
    }


    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.calendar_add_popupwindow, null);


        }
        // set windows

        popupWindow =  new PopupWindow(view, android.view.ViewGroup.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT ,true);

        // set disappear out of windows
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        //WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //int xPos =  (windowManager.getDefaultDisplay().getHeight() / 2  - 100);

        popupWindow.showAtLocation(view, Gravity.LEFT ,0,-400);


        dowrite = (EditText)view.findViewById(R.id.popup_write);
        dotimepicker1 = (EditText)view.findViewById(R.id.popup_picktime1);
        dotimepicker2 = (EditText)view.findViewById(R.id.popup_picktime2);
        Button dobutton = (Button)view.findViewById(R.id.poppup_button);
        int t = month.get(Calendar.HOUR_OF_DAY);
        dotimepicker1.setHint(Integer.toString(t));
        dotimepicker2.setHint("00");


        dobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String s1 = dowrite.getText().toString();
                String s2 = dotimepicker1.getText().toString();
                String s3 = dotimepicker2.getText().toString();
                if(s2.equals("")){
                    s2 = ""+month.get(Calendar.HOUR_OF_DAY);
                }
                if(s3.equals("")){
                    s3 = "00";
                }
                String s4 ="time = " + s2 + " : " +s3;
                sqlite.model.Calendar calendar = new sqlite.model.Calendar(s1 , s4 ,daynow);
                long id=db3.addCalendar(calendar);

                calendar.setId(id);
                db3.updateCalendar(calendar);
                Log.e("updateCal","id"+calendar.getId());

                //resetList();
                getData(s1, s4 ,id);
                month.set((month.get(Calendar.YEAR)),month.get(Calendar.MONTH),today);
                refreshCalendar();

                dotimepicker1.setText("");
                dotimepicker2.setText("");
                dowrite.setText("");
                if (popupWindow != null) {
                   popupWindow.dismiss();
                }

            }

        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_view, menu);
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

    //public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

    //}
}
