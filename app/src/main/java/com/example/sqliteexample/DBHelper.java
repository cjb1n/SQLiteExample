package com.example.sqliteexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "choi.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //데이터베이스가 생성이 될 때 호출

        //데이터베이스 ->테이블 ->컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDATE TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    //SELECT 문 (할일 목록들을 조회)
    public ArrayList<TodoItem> getTodolist(){
        ArrayList<TodoItem> todoItems= new ArrayList<>();

        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC",null);
        //select 조회 todolist에 있는 모든것을 조회 order by - 정렬을할 때  desc-내림차순 =writedate를 내림차순으로 정렬.
        if (cursor.getCount() !=0){
            // 조회한 데이터가 있을때 내부수행
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDATE = cursor.getString(cursor.getColumnIndex("writeDATE"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDATE);
                todoItems.add(todoItem);
            }
        }
        cursor.close();

        return todoItems;
    }


    //INSERT 문(할일 목록을 db에 넣는다)
    public void InsertTodo(String _title,String _content,String _writeDATE){
        SQLiteDatabase db = getWritableDatabase();  //적을 수 있는 table
        db.execSQL("INSERT INTO TodoList(title, content,writeDATE) VALUES('"+ _title + "' ,'"+ _content+ "' , '"+ _writeDATE + "');");
    }

    // update문(할일 목록을 수정한다.)
    public void UpdateTodo(String _title, String _content, String _writeDATE, String _beforedate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Todolist SET title='"+_title + "', content='"+_content + "' ,  writeDate='"+_writeDATE + "'  WHERE writedate= '"+ _beforedate + "' " );
    }


    // delete문(할일 목록을 제거한다.)
    public void deletetodo(String _beforedate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writedate ='" + _beforedate +"'");
    }

}
