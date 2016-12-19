package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import model.MyNote;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<MyNote> noteList = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME +
                                    "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                                    Constants.TITLE_NAME + " TEXT, " + Constants.CONTENT_NAME +
                                    " TEXT, " + Constants.DATE_NAME + " LONG);";

        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    public void addNotes(MyNote note){

        SQLiteDatabase db = this.getWritableDatabase();

        //Similar to hashmap
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);

        Log.v("NOTES SAVED", "YEAH");
        db.close();
    }

    public ArrayList<MyNote> getNotes(){
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        //Similar to cursor in a cell of a table
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                                Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME},
                                null, null, null, null, Constants.DATE_NAME + " DESC");

        //loop through the cursor
        if(cursor.moveToFirst()){
            //Todo: Try to change while loop is there's error
            while(!cursor.isLast()){

                MyNote note = new MyNote();
                String titleCol = cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME));
                String contentCol = cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME));
                int itemId = cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID));
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                note.setTitle(titleCol);
                note.setContent(contentCol);
                note.setItemId(itemId);
                note.setRecordDate(date);
                noteList.add(note);
                cursor.moveToNext();
            }

        }

        return noteList;
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();
    }
}
