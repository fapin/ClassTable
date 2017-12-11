/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.classable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.fapin.classtable.R;
import com.fapin.helper.DataAdapterForClassConfigClass;
import com.fapin.helper.DatabaseHelper;
import com.fapin.helper.LogUtil;
import com.fapin.helper.ModifyClassDialog;

public class ClassConfigActivity extends Activity {

    private ListView mDataListView;

    private DatabaseHelper mDatabaseHelper = null;

    private DataAdapterForClassConfigClass mDataAdapterForClassConfigClass;

    private LinearLayout mAddClassLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_config);
        init();
    }

    private List<Map<String, String>> list = null;

    private int classId = 0;

    private void init() {
        mAddClassLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_date_classconfig);
        mAddClassLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                LogUtil.d("mAddClassLinearLayout.setOnClickListener");
                mHandler.sendEmptyMessage(MSG_ADD_CALSS_TO_DB);
            }
        });
        mDatabaseHelper = new DatabaseHelper(ClassConfigActivity.this);
        mDataListView = (ListView) findViewById(R.id.listView_data_classconfig);
        refreshListView();
        mDataListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Adapter adapter = parent.getAdapter();
                @SuppressWarnings("unchecked")
                Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                String classname = map.get("classname").toString();
                String teacher = map.get("teacher").toString();
                String details = getString(R.string.title_curriculum_details) + ": " + classname
                        + "\n" + getString(R.string.class_teacher) + ":\n" + teacher;
                new AlertDialog.Builder(ClassConfigActivity.this)
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
                String classname = map.get("classname").toString();
                String teacher = map.get("teacher").toString();
                LogUtil.d(classname + " | " + teacher);
                Cursor cursor = mDatabaseHelper.queryClassTablebyClassNameAndTeacher(classname,
                        teacher);
                if (cursor.moveToFirst()) {
                    classId = cursor.getInt(cursor.getColumnIndex("classid"));
                }

                String[] items = new String[] {
                        getString(R.string.update_info), getString(R.string.delete_info)
                };
                new AlertDialog.Builder(ClassConfigActivity.this).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.d("which = " + which);
                                switch (which) {
                                    // update_info
                                    case 0:
                                        final ModifyClassDialog.Builder builder = new ModifyClassDialog.Builder(
                                                ClassConfigActivity.this);
                                        builder.setTitle(getString(R.string.class_update));
                                        builder.setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                            int which) {
                                                        String classModify = "";
                                                        String teacherModify = "";
                                                        classModify = builder
                                                                .getEditTextById(R.id.editText_class_name);
                                                        teacherModify = builder
                                                                .getEditTextById(R.id.editText_class_teacher);
                                                        map.put("classname", classModify);
                                                        map.put("teacher", teacherModify);
                                                        list.set(position, map);
                                                        mDataAdapterForClassConfigClass.getView(
                                                                position, view, mDataListView);
                                                        String[] whereArgs = {
                                                            String.valueOf(classId)
                                                        };
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put("classname", classModify);
                                                        contentValues.put("teacher", teacherModify);
                                                        mDatabaseHelper
                                                                .update(DatabaseHelper.CLASSTABLE,
                                                                        "classid", whereArgs,
                                                                        contentValues);
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
                                    // delete_info
                                    case 1:
                                        String[] whereArgs = {
                                            String.valueOf(classId)
                                        };
                                        mDatabaseHelper.delete(DatabaseHelper.CLASSTABLE,
                                                "classid", whereArgs);
                                        mHandler.sendEmptyMessage(MSG_REFRESH_LISTVIEW);
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
        Cursor cursorClassTable = null;
        String classname = null;
        String teacher = null;
        cursorClassTable = mDatabaseHelper.queryAllData(DatabaseHelper.CLASSTABLE);
        if (cursorClassTable != null && cursorClassTable.getCount() > 0) {
            while (cursorClassTable.moveToNext()) {
                map = new HashMap<String, String>(2);
                classname = cursorClassTable
                        .getString(cursorClassTable.getColumnIndex("classname"));
                teacher = cursorClassTable.getString(cursorClassTable.getColumnIndex("teacher"));
                map.put("classname", classname);
                map.put("teacher", teacher);
                list.add(map);
                map = null;
            }
        }
        return list;
    }

    private void refreshListView() {
        if (null != mDataAdapterForClassConfigClass) {
            mDataAdapterForClassConfigClass = null;
        }
        list = getData();
        mDataAdapterForClassConfigClass = new DataAdapterForClassConfigClass(this, list);
        mDataListView.setAdapter(mDataAdapterForClassConfigClass);
    }

    private static final int MSG_ADD_CALSS_TO_DB = 0;

    private static final int MSG_REFRESH_LISTVIEW = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_ADD_CALSS_TO_DB:
                    final ModifyClassDialog.Builder builder = new ModifyClassDialog.Builder(
                            ClassConfigActivity.this);
                    builder.setTitle(getString(R.string.class_add));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String className = builder.getEditTextById(R.id.editText_class_name);
                            String teacher = builder.getEditTextById(R.id.editText_class_teacher);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("classname", className);
                            contentValues.put("teacher", teacher);
                            mDatabaseHelper.insert(DatabaseHelper.CLASSTABLE, contentValues);
                            contentValues.clear();
                            contentValues = null;
                            mHandler.sendEmptyMessage(MSG_REFRESH_LISTVIEW);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(R.string.cancel,
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builder.create().show();
                    break;
                case MSG_REFRESH_LISTVIEW:
                    refreshListView();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

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
