package com.example.Journeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Tyson on 10/2/2014.
 */
public class JourneyDatabaseAdapter{

   JourneyHelper helper;

   public JourneyDatabaseAdapter(Context context){

       helper = new JourneyHelper(context);

   }
   public long insertData(String jName){

       SQLiteDatabase db = helper.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(JourneyHelper.NAME, jName);
       long id =db.insert(JourneyHelper.TABLE_NAME, null, contentValues);
       return id;
   }

   public void deleteRow(){
        // DELETE * FROM JourneyTable Where Name ='name'
       SQLiteDatabase db = helper.getWritableDatabase();
       String[] whereArgs = {"name"};
       db.delete(helper.TABLE_NAME, helper.NAME+ " =?", whereArgs  );


   }


    public String getData(){

        StringBuffer buffer = new StringBuffer();

        SQLiteDatabase db= helper.getWritableDatabase();
        /* select _id, name from JourneyTable*/
        String[] columns = {helper.UID, helper.NAME};

        Cursor cursor =db.query(helper.TABLE_NAME, columns, null, null, null, null, null);

        while(cursor.moveToNext()){

         //   int index1 = cursor.getColumnIndex(helper.UID);
           int cid = cursor.getInt(0);
           String name = cursor.getString(1);
           buffer.append(cid + " "+name+" \n") ;
        }
        return buffer.toString();
    }
   static class JourneyHelper extends SQLiteOpenHelper  {


       private static final String DATABASE_NAME = "journeydatabase.db";
       private static final String TABLE_NAME = "JourneyTable";
       private static final String UID="_id";
       private static final String NAME = "Journey Name";
       private static final int DATABASE_VERSION=1;
       private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT);";

       JourneyHelper(Context context)
       {
           super ( context, DATABASE_NAME, null, DATABASE_VERSION);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {


           db.execSQL(CREATE_TABLE);
           

       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

           db.execSQL("DROP TABLE IF EXISTS JourneyTable");

           onCreate(db);
       }
   }
}
