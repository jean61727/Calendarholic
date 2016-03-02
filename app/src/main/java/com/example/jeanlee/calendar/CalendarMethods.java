package com.example.jeanlee.calendar;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.roomorama.caldroid.CaldroidFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Calendar;

/**
 * Created by jeanlee on 2014/12/27.
 */
public class CalendarMethods{

    public void calembed(){
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

       /** FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();**/

    }

}
