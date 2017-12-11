/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.classable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fapin.classtable.R;
import com.fapin.helper.DataAdapterForWeeklyConfigClass;
import com.fapin.helper.DatabaseHelper;
import com.fapin.helper.LogUtil;
import com.fapin.helper.ModifyWeeklyDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class WeeklyConfigActivity extends Activity {

    private ListView mDataListView;

    private DatabaseHelper mDatabaseHelper = null;

    private static final int WEEKLY_SUNDAY = 1;

    private static final int WEEKLY_MONDAY = 2;

    private static final int WEEKLY_TUESDAY = 3;

    private static final int WEEKLY_WEDNESDAY = 4;

    private static final int WEEKLY_THURSDAY = 5;

    private static final int WEEKLY_FRIDAY = 6;

    private static final int WEEKLY_SATURDAY = 7;

    private int weeklyDay = WEEKLY_SUNDAY;

    private TextView mWeeklyDayShowTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_config);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu_weekly, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_config_sunday:
                weeklyDay = WEEKLY_SUNDAY;
                mWeeklyDayShowTextView.setText(R.string.sunday);
                break;
            case R.id.menu_config_monday:
                weeklyDay = WEEKLY_MONDAY;
                mWeeklyDayShowTextView.setText(R.string.monday);
                break;
            case R.id.menu_config_tuesday:
                weeklyDay = WEEKLY_TUESDAY;
                mWeeklyDayShowTextView.setText(R.string.tuesday);
                break;
            case R.id.menu_config_wednesday:
                weeklyDay = WEEKLY_WEDNESDAY;
                mWeeklyDayShowTextView.setText(R.string.wednesday);
                break;
            case R.id.menu_config_thursday:
                weeklyDay = WEEKLY_THURSDAY;
                mWeeklyDayShowTextView.setText(R.string.thursday);
                break;
            case R.id.menu_config_friday:
                weeklyDay = WEEKLY_FRIDAY;
                mWeeklyDayShowTextView.setText(R.string.friday);
                break;
            case R.id.menu_config_saturday:
                weeklyDay = WEEKLY_SATURDAY;
                mWeeklyDayShowTextView.setText(R.string.saturday);
                break;
            default:
                break;
        }
        refreshData();
        return super.onOptionsItemSelected(item);
    }

    private DataAdapterForWeeklyConfigClass mDataAdapterForWeeklyConfigClass;

    private void refreshData() {
        if (null != mDataAdapterForWeeklyConfigClass) {
            mDataAdapterForWeeklyConfigClass = null;
        }
        list = getData(weeklyDay);
        mDataAdapterForWeeklyConfigClass = new DataAdapterForWeeklyConfigClass(this, list);
        mDataListView.setAdapter(mDataAdapterForWeeklyConfigClass);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private List<Map<String, String>> list = null;

    private void init() {
        mWeeklyDayShowTextView = (TextView) findViewById(R.id.textview_weekly_date);
        mWeeklyDayShowTextView.setText(R.string.sunday);
        mDatabaseHelper = new DatabaseHelper(WeeklyConfigActivity.this);
        // testData();
        mDataListView = (ListView) findViewById(R.id.listView_weekly_config);
        refreshData();
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
                new AlertDialog.Builder(WeeklyConfigActivity.this)
                        .setTitle(getString(R.string.detail_info)).setMessage(details).show();
                LogUtil.d("position = " + position + " | id = " + id);
            }
        });
        mDataListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                    final int position, long id) {
                // TODO Auto-generated method stub
                Adapter adapter = parent.getAdapter();
                @SuppressWarnings("unchecked")
                final Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                final String time = map.get("time").toString();
                String curriculumDetails = map.get("curriculumDetails").toString();
                String address = map.get("address").toString();
                LogUtil.d(time + " | " + curriculumDetails + " | " + address);
                String[] items = new String[] {
                        getString(R.string.update_info), getString(R.string.delete_info)
                };
                new AlertDialog.Builder(WeeklyConfigActivity.this).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.d("which = " + which);
                                switch (which) {
                                // update_info
                                    case 0:
                                        final ModifyWeeklyDialog.Builder builder = new ModifyWeeklyDialog.Builder(
                                                WeeklyConfigActivity.this);
                                        builder.setTitle(getString(R.string.title_modify) + " | "
                                                + time);
                                        builder.setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                            int which) {
                                                        int classId = builder.getClassId();
                                                        String className = builder.getClassName();
                                                        String teacher = builder.getClassTeacher();
                                                        String address = builder
                                                                .getEditTextById(R.id.editText_class_address);
                                                        map.put("curriculumDetails", className
                                                                + "--" + teacher);
                                                        map.put("address", address);
                                                        list.set(position, map);
                                                        mDataAdapterForWeeklyConfigClass.getView(
                                                                position, view, mDataListView);
                                                        int timeId = mDatabaseHelper
                                                                .queryTimeIdByTime(time);
                                                        mDatabaseHelper
                                                                .updateWeeklyTable(weeklyDay,
                                                                        timeId, classId, address);
                                                        dialog.dismiss();
                                                    }
                                                });

                                        builder.setNegativeButton(
                                                R.string.cancel,
                                                new android.content.DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                        builder.create().show();
                                        break;
                                    // delete_info
                                    case 1:

                                        break;

                                    default:
                                        break;
                                }
                            }
                        }).show();
                LogUtil.d("position = " + position + " | id = " + id);
                return true;
            }

        });

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
