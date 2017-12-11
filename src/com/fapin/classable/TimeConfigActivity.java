/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.classable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fapin.helper.DataAdapterForTimeConfigClass;
import com.fapin.helper.DatabaseHelper;
import com.fapin.helper.LogUtil;
import com.fapin.helper.ModifyTimeDialog;
import com.fapin.classtable.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class TimeConfigActivity extends Activity {

    private ListView mDataListView;

    private DatabaseHelper mDatabaseHelper = null;

    private DataAdapterForTimeConfigClass mDataAdapterForTimeConfigClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_config);
        init();
    }

    private List<Map<String, String>> list = null;

    private void init() {
        mDatabaseHelper = new DatabaseHelper(TimeConfigActivity.this);
        mDataListView = (ListView) findViewById(R.id.listView_data_timeconfig);
        list = getData();
        mDataAdapterForTimeConfigClass = new DataAdapterForTimeConfigClass(this, list);
        mDataListView.setAdapter(mDataAdapterForTimeConfigClass);
        mDataListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                    final int position, long id) {
                // TODO Auto-generated method stub
                Adapter adapter = parent.getAdapter();
                @SuppressWarnings("unchecked")
                final Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                final String timeid = map.get("timeid").toString();
                String time = map.get("time").toString();
                LogUtil.d(timeid + " | " + time);
                String[] items = new String[] {
                    getString(R.string.update_info)
                };
                new AlertDialog.Builder(TimeConfigActivity.this).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.d("which = " + which);
                                switch (which) {
                                // update_info
                                    case 0:
                                        final ModifyTimeDialog.Builder builder = new ModifyTimeDialog.Builder(
                                                TimeConfigActivity.this);
                                        builder.setTitle(getString(R.string.title_modify) + " | "
                                                + timeid);
                                        builder.setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                            int which) {
                                                        map.put("timeid", timeid);
                                                        String timeTomodify = null;
                                                        timeTomodify = builder
                                                                .getEditTextById(R.id.editText_time_start)
                                                                + "-"
                                                                + builder
                                                                        .getEditTextById(R.id.editText_time_end);
                                                        map.put("time", timeTomodify);
                                                        list.set(position, map);
                                                        mDataAdapterForTimeConfigClass.getView(
                                                                position, view, mDataListView);
                                                        String[] whereArgs = {
                                                            String.valueOf(position + 1)
                                                        };
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put("time", timeTomodify);
                                                        mDatabaseHelper.update(
                                                                DatabaseHelper.TIMETABLE, "timeid",
                                                                whereArgs, contentValues);
                                                        contentValues.clear();
                                                        contentValues = null;
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

    public List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        Cursor cursorTimeTable = null;
        int timeId = 0;
        String time = null;
        cursorTimeTable = mDatabaseHelper.queryAllData(DatabaseHelper.TIMETABLE);
        if (cursorTimeTable != null && cursorTimeTable.getCount() > 0) {
            while (cursorTimeTable.moveToNext()) {
                map = new HashMap<String, String>(2);
                timeId = cursorTimeTable.getInt(cursorTimeTable.getColumnIndex("timeid"));
                time = cursorTimeTable.getString(cursorTimeTable.getColumnIndex("time"));
                map.put("timeid", changeTimeIdToString(timeId));
                map.put("time", time);
                list.add(map);
                map = null;
            }
        }
        return list;
    }

    private String changeTimeIdToString(int timeId) {
        String result = null;
        switch (timeId) {
            case 1:
                result = getString(R.string.str_class1);
                break;
            case 2:
                result = getString(R.string.str_class2);
                break;
            case 3:
                result = getString(R.string.str_class3);
                break;
            case 4:
                result = getString(R.string.str_class4);
                break;
            case 5:
                result = getString(R.string.str_class5);
                break;
            case 6:
                result = getString(R.string.str_class6);
                break;

            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
