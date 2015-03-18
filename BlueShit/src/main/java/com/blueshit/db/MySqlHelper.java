package com.blueshit.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Jayden on 2015/3/4.
 */
public class MySqlHelper {
    public MyDatabaseHelper mOpenHelper;
    public SQLiteDatabase db;
    //构造函数
    public MySqlHelper(Context context)
    {
        mOpenHelper = new MyDatabaseHelper(context,"t_user",null,1);
    }
    //选择
    public int selectItem(String mytitle)
    {
        db = mOpenHelper.getReadableDatabase();
        //String col[] = {"Time","Title","Content"};
        //String Sql = "where 'Title' = " + mytitle;
        //Cursor cur = db.query("Table_1",col,Sql,null,null,null,null);
        Cursor cur = db.rawQuery("SELECT * FROM Table_1 WHERE Title=?", new String[] { mytitle});
        int num = cur.getCount();
        cur.close();
        db.close();
        return num;
    }
    //插入
    public void insertItem(String username,String password)
    {
        db = mOpenHelper.getWritableDatabase();
        String Sql= "insert into t_user(username,password) values('" + username +"', '" + password +"' )";
        try
        {
            db.execSQL(Sql);
            //setTitle("Yes");
        }
        catch(SQLException e)
        {
            //setTitle(Sql);
        }
        db.close();
    }
    //显示
    public ArrayList<HashMap<String, Object>> showItem()
    {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        db = mOpenHelper.getReadableDatabase();
        String col[] = {"Time","Title","Content"};
        Cursor cur = db.query("Table_1",col,null,null,null,null,"Time desc"); //按时间降序
        int num = cur.getCount();
        //setTitle(Integer.toString(num) + " 条记录");
        cur.moveToFirst();
        for(int i=0;i<num;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", cur.getString(1));
            map.put("ItemText", cur.getString(0));
            listItem.add(map);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return listItem;
    }
    //删除Item
    public void deleteItem(String mytitle)
    {
        try
        {
            db = mOpenHelper.getWritableDatabase();
            db.delete("Table_1","Title = '"+mytitle + "'",null);
            //setTitle("删除Title为" + mytitle +"的记录");
            db.close();
        }
        catch(SQLException e)
        {}
    }
    //删除表项
    public void deleteTable(String mytable)
    {
        try
        {
            db = mOpenHelper.getWritableDatabase();
            String Sql = "drop table " +mytable;
            db.execSQL(Sql);
            db.close();
            //setTitle("已删除" + mytable +"表项");
        }
        catch(SQLException e)
        {}
    }
}
