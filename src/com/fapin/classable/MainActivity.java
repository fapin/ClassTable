/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.classable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fapin.helper.DataAdapterForWeeklyConfigClass;
import com.fapin.helper.DatabaseHelper;
import com.fapin.helper.LogUtil;
import com.fapin.classtable.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

public class MainActivity extends Activity {

    private int mYear, mMonth, mDay, mDayOfWeek;

    private Button mDateBefore, mDateButton, mDateNext;

    private ListView mDataListView;

    private static final int DATE_DIALOG = 1;

    private DatabaseHelper mDatabaseHelper = null;

    private List<Map<String, String>> list = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_config_time:
                skipTo(TimeConfigActivity.class);
                break;
            case R.id.menu_config_class:
                skipTo(ClassConfigActivity.class);
                break;
            case R.id.menu_config_weekly:
                skipTo(WeeklyConfigActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void skipTo(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LogUtil.d("calendar");
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar = null;
        mHandler.sendEmptyMessage(MSG_DATESET_COMPLETED);
    }

    private void init() {
        mDatabaseHelper = new DatabaseHelper(MainActivity.this);

        mDateBefore = (Button) findViewById(R.id.button_datebefore);
        mDateBefore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.d("mDateBefore");
                mDay = mDay - 1;
                mHandler.sendEmptyMessage(MSG_DATESET_COMPLETED);
            }
        });
        mDateButton = (Button) findViewById(R.id.button_datechoose);
        mDateButton.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                LogUtil.d("mDateButton");
                showDialog(DATE_DIALOG);
            }
        });
        mDateNext = (Button) findViewById(R.id.button_datenext);
        mDateNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.d("mDateNext");
                mDay = mDay + 1;
                mHandler.sendEmptyMessage(MSG_DATESET_COMPLETED);
            }
        });
        mDataListView = (ListView) findViewById(R.id.listView_weekly_data);
        mDataListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Adapter adapter = parent.getAdapter();
                @SuppressWarnings("unchecked")
                Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                String time = map.get("time").toString();
                String curriculumDetails = map.get("curriculumDetails").toString();
                String address = map.get("address").toString();
                String details = getString(R.string.title_time) + ": " + time + "\n"
                        + getString(R.string.title_curriculum_details) + ":\n" + curriculumDetails
                        + "\n" + getString(R.string.title_address) + ": " + address;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.detail_info)).setMessage(details).show();
                LogUtil.d("position = " + position + " | id = " + id);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
            default:
                break;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            mHandler.sendEmptyMessage(MSG_DATESET_COMPLETED);
        }
    };

    private static final int MSG_DATESET_COMPLETED = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_DATESET_COMPLETED:
                    String weekday = getWeek(mYear + "-" + (mMonth + 1) + "-" + mDay);
                    mDateButton.setText(mYear + "/" + (mMonth + 1) + "/" + mDay + "(" + weekday
                            + ")");
                    refreshData();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 判断当前日期是星期几
     * 
     * @param pTime 设置的需要判断的时间 //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    @SuppressLint("SimpleDateFormat")
    private String getWeek(String pTime) {

        String week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (mDayOfWeek) {
            case 1:
                week = getString(R.string.sunday);
                break;
            case 2:
                week = getString(R.string.monday);
                break;
            case 3:
                week = getString(R.string.tuesday);
                break;
            case 4:
                week = getString(R.string.wednesday);
                break;
            case 5:
                week = getString(R.string.thursday);
                break;
            case 6:
                week = getString(R.string.friday);
                break;
            case 7:
                week = getString(R.string.saturday);
                break;

            default:
                break;
        }

        return week;
    }

    private DataAdapterForWeeklyConfigClass mDataAdapterForWeeklyConfigClass;

    private void refreshData() {
        if (null != mDataAdapterForWeeklyConfigClass) {
            mDataAdapterForWeeklyConfigClass = null;
        }
        list = getData(mDayOfWeek);
        mDataAdapterForWeeklyConfigClass = new DataAdapterForWeeklyConfigClass(this, list);
        mDataListView.setAdapter(mDataAdapterForWeeklyConfigClass);
    }

    public List<Map<String, String>> getData(int weekId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        Cursor cursorWeeklyTable = null;
        Cursor cursorTimeTable = null;
        Cursor cursorClassTable = null;
        int id = 0, weekid = 0, timeid = 0, classid = 0;
        String address = "", time = "", classname = "", teacher = "";
        try {
            cursorWeeklyTable = mDatabaseHelper.queryWeeklyTableWeekId(weekId);
            if (cursorWeeklyTable != null && cursorWeeklyTable.getCount() > 0) {
                while (cursorWeeklyTable.moveToNext()) {
                    map = new HashMap<String, String>(2);
                    id = cursorWeeklyTable.getInt(cursorWeeklyTable.getColumnIndex("id"));
                    weekid = cursorWeeklyTable.getInt(cursorWeeklyTable.getColumnIndex("weekid"));
                    timeid = cursorWeeklyTable.getInt(cursorWeeklyTable.getColumnIndex("timeid"));
                    classid = cursorWeeklyTable.getInt(cursorWeeklyTable.getColumnIndex("classid"));
                    address = cursorWeeklyTable.getString(cursorWeeklyTable
                            .getColumnIndex("address"));
                    LogUtil.d(id + " | " + weekid + " | " + timeid + " | " + classid + " | "
                            + address);
                    cursorTimeTable = mDatabaseHelper.queryTimeTableByTimeId(timeid);
                    if (cursorTimeTable != null && cursorTimeTable.getCount() > 0) {
                        if (cursorTimeTable.moveToFirst()) {
                            time = cursorTimeTable
                                    .getString(cursorTimeTable.getColumnIndex("time"));
                        }
                    }
                    cursorClassTable = mDatabaseHelper.queryClassTablebyClassId(classid);
                    if (cursorClassTable != null && cursorClassTable.getCount() > 0) {
                        if (cursorClassTable.moveToFirst()) {
                            classname = cursorClassTable.getString(cursorClassTable
                                    .getColumnIndex("classname"));
                            teacher = cursorClassTable.getString(cursorClassTable
                                    .getColumnIndex("teacher"));
                        }
                    }
                    map.put("time", time);
                    map.put("curriculumDetails", classname + "--" + teacher);
                    map.put("address", address);
                    list.add(map);
                    map = null;
                    address = "";
                    time = "";
                    classname = "";
                    teacher = "";
                }
            }
        } catch (SQLException e) {
            LogUtil.e("queryDatas" + e.toString());
        }
        if (cursorWeeklyTable != null) {
            cursorWeeklyTable.close();
            cursorWeeklyTable = null;
        }
        if (cursorTimeTable != null) {
            cursorTimeTable.close();
            cursorTimeTable = null;
        }
        if (cursorClassTable != null) {
            cursorClassTable.close();
            cursorClassTable = null;
        }
        return list;
    }

}
