package com.app.oliver.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private boolean initFlag = false;
    private Cursor allQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDB();

        insertDB();

        queryDB();

        initView();

    }

    private void initView() {
        ListView lvResultSet = (ListView) findViewById(R.id.lvResultSet);

        lvResultSet.setAdapter(new MyCursorAdapter(this,allQuery,true));

    }

    class MyCursorAdapter extends CursorAdapter {
        public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public MyCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return View.inflate(context, R.layout.result_cell, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvKey = (TextView) view.findViewById(R.id.tvKey);
            TextView tvValue  = (TextView) view.findViewById(R.id.tvValue);

            if(cursor.moveToNext()){
                tvKey.setText(cursor.getString(cursor.getColumnIndex("key")));
                tvValue.setText(cursor.getString(cursor.getColumnIndex("value")));
            }
        }
    }

    private void queryDB() {
        Log.d("test","start query");
        String querySql = "select * from maps";
        Cursor c = db.rawQuery(querySql, null);
        allQuery = db.rawQuery(querySql, null);



        while(c.moveToNext()){
            String result =
                c.getInt(c.getColumnIndex("_id")) + " " +
                c.getString(c.getColumnIndex("key"))+ " " +
                c.getString(c.getColumnIndex("value"));

            Log.d("test", result);
        }


    }

    private void insertDB() {
        String insert = "insert or replace into contacts values(null,?,?)";
        String[][] values= {
                {"mike","123456789"},
                {"like","321654987"},
                {"hack","789456123"}
        };


        if(initFlag)
            for(String[] a:values) {
                db.execSQL(insert,a);
            }
    }

    private boolean isTableExist(String tableName) {
        String ifExistTable = "select count(1) from sqlite_sequence where name=?";
        Cursor c = db.rawQuery(ifExistTable, new String[]{tableName});
        c.moveToNext();

        return c.getInt(0) == 1;

        /*if(c.getInt(0) == 1){
            Log.d("test", "exist table");
        }else{
            Log.d("test","not exist");
        }*/
    }

    private void initDB() {
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "Remember.db";

        db = SQLiteDatabase.openOrCreateDatabase(path,null);

//        db = openOrCreateDatabase("testDatabase",MODE_PRIVATE, null );

//        initFlag = !isTableExist("contacts");

//        String sql = "create table if not exists contacts(_id integer primary key autoincrement," +
//                                                                 "name varchar(20) not null," +
//                                                                "tel varchar(15))";
//        db.execSQL(sql);
    }
}
