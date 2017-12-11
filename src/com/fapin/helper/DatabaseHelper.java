/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NAME = "Table";

    private static final int VERSION = 1;

    public static final String CLASSTABLE = "class_table";

    public static final String TIMETABLE = "time_table";

    public static final String WEEKLYTABLE = "weekly_table";

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS class_table (classid integer primary key autoincrement, classname varchar(100), teacher varchar(50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS time_table (timeid integer primary key, time varchar(100))");
        db.execSQL("CREATE TABLE IF NOT EXISTS weekly_table (id integer primary key autoincrement, weekid integer, timeid integer, classid integer, address varchar(100))");

        initTimeData(db);
        initWeeklyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static final int TIME_DATA_SIZE = 6;

    private void initTimeData(SQLiteDatabase db) {
        ContentValues contentValues = null;
        for (int i = 0; i < TIME_DATA_SIZE; i++) {
            contentValues = new ContentValues();
            contentValues.put("timeid", i + 1);
            contentValues.put("time", "");
            db.insert("time_table", null, contentValues);
            contentValues = null;
        }
    }

    private static final int WEEK_DATA_SIZE = 7;

    private void initWeeklyData(SQLiteDatabase db) {
        ContentValues contentValues = null;
        for (int i = 0; i < WEEK_DATA_SIZE; i++) {
            for (int j = 0; j < TIME_DATA_SIZE; j++) {
                contentValues = new ContentValues();
                contentValues.put("weekid", i + 1);
                contentValues.put("timeid", j + 1);
                contentValues.put("classid", 0);
                contentValues.put("address", "");
                db.insert("weekly_table", null, contentValues);
                contentValues = null;
            }
        }
    }

    /**
     * 插入一条数据
     * 
     * @param table
     * @param contentValues
     */
    public void insert(String table, ContentValues contentValues) {
        getWritableDatabase().insert(table, null, contentValues);
    }

    /**
     * 删除一条数据
     * 
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        getReadableDatabase().delete(table, whereClause + "=?", whereArgs);
    }

    public void update(String table, String whereClause, String[] whereArgs,
            ContentValues contentValues) {
        getWritableDatabase().update(table, contentValues, whereClause + "=?", whereArgs);
    }

    public void updateWeeklyTable(int weekId, int timeId, int classId, String address) {
        String updateSql = "update weekly_table Set classId ='" + classId + "',address ='"
                + address + "' where weekid='" + weekId + "'and timeid='" + timeId + "'";
        getWritableDatabase().execSQL(updateSql);
    }

    public Cursor queryAllData(String table) {
        Cursor cursor = null;
        cursor = getReadableDatabase().query(table, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor queryClassTablebyClassId(int classid) {
        Cursor cursor = null;
        cursor = getReadableDatabase().query("class_table", new String[] {
            "classid,classname,teacher"
        }, "classid=?", new String[] {
            classid + ""
        }, null, null, null);
        return cursor;
    }

    public Cursor queryClassTablebyClassNameAndTeacher(String className, String teacher) {
        Cursor cursor = null;
        cursor = getReadableDatabase().query("class_table", new String[] {
            "classid,classname,teacher"
        }, "classname=? And teacher=?", new String[] {
                className, teacher
        }, null, null, null);
        return cursor;
    }

    public Cursor queryTimeTableByTimeId(int timeid) {
        Cursor cursor = null;
        cursor = getReadableDatabase().query("time_table", new String[] {
            "timeid,time"
        }, "timeid=?", new String[] {
            timeid + ""
        }, null, null, null);
        return cursor;
    }

    public Cursor queryWeeklyTableWeekId(int weekid) {
        Cursor cursor = null;
        cursor = getReadableDatabase().query("weekly_table", new String[] {
            "id,weekid,timeid,classid,address"
        }, "weekid=?", new String[] {
            weekid + ""
        }, null, null, null);
        return cursor;
    }

    public int queryTimeIdByTime(String time) {
        int timeId = -1;
        Cursor cursor = null;
        cursor = getReadableDatabase().query("time_table", new String[] {
            "timeid,time"
        }, "time=?", new String[] {
            time + ""
        }, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                timeId = cursor.getInt(cursor.getColumnIndex("timeid"));
            }
        }
        return timeId;
    }
}
