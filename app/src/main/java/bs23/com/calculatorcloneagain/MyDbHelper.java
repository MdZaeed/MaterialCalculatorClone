package bs23.com.calculatorcloneagain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.TimeUtils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by BS-86 on 2/8/2016.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase db1;
    private Context context;
    public static String DATABASE_NAME="CalculatorEntries.db";
    public static String TABLE_NAME="CalculatorHistory";
    public static String TABLE_NAME_SECOND="IndexTable";
    public static String COLOUMN_NAME_ID="id";
    public static String COLOUMN_NAME_EQUATION="equation";
    public static String COLOUMN_NAME_RESULT="result";
    public static String COLOUMN_NAME_TIME="time";
    public static String COLOUMN_NAME_INDEX="indexCol";
    public static String COLOUMN_NAME_ID_SECOND="id";


    public static String COMMA=",";
    public static String PRIMARY_KEY_CONSTRAIN=" PRIMARY KEY";
    public static String UNIQUE_CONSTRAINTS=" AUTOINCREMENT";
    public static String VARCHAR_TYPE=" varchar(256)";
    public static String INTEGER_TYPE=" INTEGER";

    private int index=1;

    public static String CREATE_ENTRY=
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLOUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY_CONSTRAIN + UNIQUE_CONSTRAINTS  + COMMA +
                    COLOUMN_NAME_EQUATION + VARCHAR_TYPE + COMMA +
                    COLOUMN_NAME_RESULT + VARCHAR_TYPE + COMMA +
                    COLOUMN_NAME_TIME + VARCHAR_TYPE
            + " )";


    public static String CREATE_ENTRY_SECOND=
            "CREATE TABLE " + TABLE_NAME_SECOND + " (" +
                    COLOUMN_NAME_ID + INTEGER_TYPE + COMMA +
                    COLOUMN_NAME_INDEX + INTEGER_TYPE
                    + "  )";

    public static String TABLE_DELETE_ENTRY=
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static String TABLE_DELETE_ENTRY_SECOND=
            "DROP TABLE IF EXISTS " + TABLE_NAME_SECOND;

    public static String INSERT_COMMAND_SECOND=
    "INSERT INTO " + TABLE_NAME_SECOND + " (" +COLOUMN_NAME_ID_SECOND + COMMA + COLOUMN_NAME_INDEX + ") " +
            "VALUES " + " (" + 1 + COMMA + 1 + ") " ;

    MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
/*
        onCreate(this.getWritableDatabase());
*/
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_DELETE_ENTRY);
        Log.d("Table dropped", "Yes");
        db.execSQL(CREATE_ENTRY);
        db.execSQL(TABLE_DELETE_ENTRY_SECOND);
        db.execSQL(CREATE_ENTRY_SECOND);
        db.execSQL(INSERT_COMMAND_SECOND);
        db1=db;
        Log.d("Opened ", db.isOpen() + "");
        insert("i", "am");
        insert("Tai", "Zay");
        insert("lll", "jjj");
        insert("Jod", "I");
        insert("bug", "ISS");
        Log.d("Created", "Yes");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DELETE_ENTRY);
        onCreate(db);
    }

    public void insert(String equation,String result)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String TIME=dateFormat.format(date); //2014/08/06 15:59:48
        String INSERT_COMMAND_2=
                "INSERT INTO " + TABLE_NAME + " (" + COLOUMN_NAME_EQUATION + COMMA + COLOUMN_NAME_RESULT + COMMA + COLOUMN_NAME_TIME + " ) " +
                        " VALUES ('"+ equation + "','"+ result + "','"+ TIME + "')";
/*
        SQLiteDatabase db=this.getWritableDatabase();
*/
        db1.execSQL(INSERT_COMMAND_2);
        Log.d("Inserted", "Yes");
    }

    public void update(String equation,String result)
    {
        TimeZone timeZone=TimeZone.getDefault();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        Date date = new Date();
        String TIME=dateFormat.format(date); //2014/08/06 15:59:48
        String Update_COMMAND=
                "UPDATE " + TABLE_NAME +
                " SET " + COLOUMN_NAME_EQUATION + "='" + equation + "'" +COMMA+ COLOUMN_NAME_RESULT + "='" + result + "'" + COMMA + COLOUMN_NAME_TIME +"='" + TIME + "'" +
                " WHERE " + COLOUMN_NAME_ID + "=" + getIndexFromDatabase();
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(Update_COMMAND);
        Log.d("Inserted", "Yes");
        writeIndexToDatabase();
    }

/*    public ArrayList<History> chexk()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from " + TABLE_NAME, null);
        cursor.moveToFirst();
        ArrayList<History> histories=new ArrayList<History>();
        History temp;
        int in;
        for(int i=0;i<5;i++)
        {
            if((getIndexFromDatabase()+i-1)>=5)
            {
*//*
                Log.d("CHecking","Hocche na " + getIndexFromDatabase() );
*//*
                in=(getIndexFromDatabase()+i-1)-5;
            }
            else
            {
*//*
                Log.d("CHecking","Hocche");
*//*
                in=getIndexFromDatabase()+i-1;
            }
            cursor.move(in);
            temp = new History();
            temp.setEqaution(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_EQUATION)));
            temp.setResult(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_RESULT)));
            temp.setTime(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_TIME)));
            temp.setId(cursor.getInt(cursor.getColumnIndex(COLOUMN_NAME_ID)));
            Log.d("ID:",temp.getId()+"   " + temp.getEqaution());
            histories.add(temp);
            cursor.moveToFirst();
        }
        return histories;
    }*/

    public ArrayList<History> chexk()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from " + TABLE_NAME + " " + "ORDER BY " + COLOUMN_NAME_TIME + " DESC", null);
        cursor.moveToFirst();
        ArrayList<History> histories=new ArrayList<History>();
        History temp;
        int in;
        for(int i=0;i<5;i++)
        {
            temp = new History();
            temp.setEqaution(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_EQUATION)));
            temp.setResult(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_RESULT)));
            temp.setTime(cursor.getString(cursor.getColumnIndex(COLOUMN_NAME_TIME)));
            temp.setId(cursor.getInt(cursor.getColumnIndex(COLOUMN_NAME_ID)));
            Log.d("ID:",temp.getId()+"   " + temp.getEqaution());
            histories.add(temp);
            cursor.moveToNext();
        }
        return histories;
    }

    public Integer getIndexFromDatabase()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from " + TABLE_NAME_SECOND, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(COLOUMN_NAME_INDEX));
    }

    public void writeIndexToDatabase()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Integer integer=getIndexFromDatabase();
        integer=integer+1;
        Log.d("CHecking","Hocche na " + getIndexFromDatabase().intValue() + " " + integer.intValue()+1 );
        String Update_COMMAND;
        if(integer<6) {
            Log.d("Update text","less than 6");
            Update_COMMAND =
                    "UPDATE " + TABLE_NAME_SECOND +
                            " SET " + COLOUMN_NAME_INDEX + "=" + integer +
                            " WHERE " + COLOUMN_NAME_ID + "=" + 1;
        }
        else
        {
            Log.d("Update text","more than 6");
            Update_COMMAND =
                    "UPDATE " + TABLE_NAME_SECOND +
                            " SET " + COLOUMN_NAME_INDEX + "=" + 1 +
                            " WHERE " + COLOUMN_NAME_ID + "=" + 1;
        }
        Log.d("CHecking","Hocche na tarpor " + getIndexFromDatabase().intValue() + " " + integer.intValue()+1 );
        db.execSQL(Update_COMMAND);
    }

}
