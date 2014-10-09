package com.example.Journeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.sql.SQLException;

/**
 SQLite DATABASE
 */
public class JourneyDatabaseAdapter{

   JourneyHelper helper;

   public JourneyDatabaseAdapter(Context context){

       helper = new JourneyHelper(context);

   }
   public long insertDataJourney(String jName){

       SQLiteDatabase db = helper.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(JourneyHelper.NAME, jName);
       long id =db.insert(JourneyHelper.TABLE_NAME_JOURNEY, null, contentValues);
       return id;
   }
    public long insertDataPhoto(Bitmap bm, LatLng loc, String cap, String date){

       // Insert Data into Photos table
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JourneyHelper.PHTO_LAT, loc.latitude);
        contentValues.put(JourneyHelper.PHTO_LNG, loc.longitude);
        contentValues.put(JourneyHelper.PHTO_CAP, cap);
        // Convert bitmap to byte array for BLOB storage
        int bytes = bm.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bm.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();

        contentValues.put(JourneyHelper.PHTO, array);

        long id =db.insert(JourneyHelper.TABLE_NAME_PHOTO, null, contentValues);
        return id;
    }
    public long insertDataLoc(LatLng ln){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JourneyHelper.LAT, ln.latitude);
        contentValues.put(JourneyHelper.LNG, ln.longitude);


        long id =db.insert(JourneyHelper.TABLE_NAME_CHORDS, null, contentValues);
        return id;
    }

   public void deleteRow(){
        // DELETE * FROM JourneyTable Where Name ='name'
       SQLiteDatabase db = helper.getWritableDatabase();
       String[] whereArgs = {"name"};
       db.delete(helper.TABLE_NAME_JOURNEY, helper.NAME+ " =?", whereArgs  );


   }


    public Journey getData(){

        Journey buffer = new Journey();

        SQLiteDatabase db= helper.getWritableDatabase();
        /* select _id, name from JourneyTable*/
        String[] columns = {helper.UID, helper.NAME};

        Cursor cursor =db.query(helper.TABLE_NAME_JOURNEY, columns, null, null, null, null, null);

        while(cursor.moveToNext()){

        //  int index1 = cursor.getColumnIndex(helper.UID);
           int cid = cursor.getInt(0);
           String name = cursor.getString(1);

           buffer.endJourney(name);

        }
        return buffer;
    }
   static class JourneyHelper extends SQLiteOpenHelper  {


       private static final String DATABASE_NAME = "journeydatabase.db";


       //JOURNEY TABLE
       private static final String TABLE_NAME_JOURNEY = "Journey";
       private static final String UID="_id";
       private static final String NAME = "Journey Name";

       private static final String CREATE_TABLE_JOURNEY =

               "CREATE TABLE "+TABLE_NAME_JOURNEY+" " +
               "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
               +" "+NAME+" TEXT, " +
               "FOREIGN KEY(Locations) REFERENCES Locations(_id)," +
               "FOREIGN KEY(Photos) REFERENCES Photos(_id);";

      // LOCATION TABLE
       private static final String TABLE_NAME_CHORDS = "Locations";
       private static final String LAT="lat";
       private static final String LNG="lng";

       private static final String CREATE_TABLE_CHORDS =
               "CREATE TABLE "+TABLE_NAME_CHORDS+" " +
               "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "" +" "+LAT+" DOUBLE, "+LNG+" DOUBLE);";

       // PHOTO TABLE COLUMNS
       private static final String TABLE_NAME_PHOTO = "Photos";
       private static final String PHTO="photo";
       private static final String PHTO_CAP="caption";
       private static final String PHTO_LAT="photoLat";
       private static final String PHTO_LNG="photoLng";
       private static final String PHTO_DATE="date";

       private static final String CREATE_TABLE_PHOTO =
               "CREATE TABLE "+TABLE_NAME_PHOTO+" " +
               "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
               " "+PHTO+" BLOB, "+PHTO_LAT+" DOUBLE, "+PHTO_LNG+" DOUBLE," +
               " "+PHTO_CAP+" TEXT, "+PHTO_DATE+" TEXT);";

       private static final int DATABASE_VERSION=2;



       JourneyHelper(Context context)
       {
           super ( context, DATABASE_NAME, null, DATABASE_VERSION);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {



           db.execSQL(CREATE_TABLE_PHOTO);
           db.execSQL(CREATE_TABLE_CHORDS);
           db.execSQL(CREATE_TABLE_JOURNEY);
           

       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

          db.execSQL("DROP TABLE IF EXISTS JourneyTable");

           onCreate(db);
       }
   }
}
