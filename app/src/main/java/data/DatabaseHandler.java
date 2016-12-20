package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        super(context, MyNote.DATABASE_NAME, null, MyNote.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + MyNote.TABLE_NAME +
                                    "(" + MyNote.KEY_ID + " INTEGER PRIMARY KEY," +
                                    MyNote.TITLE_NAME + " TEXT, " + MyNote.CONTENT_NAME +
                                    " TEXT, " + MyNote.DATE_NAME + " LONG);";

        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MyNote.TABLE_NAME);

        onCreate(db);

    }

    public void addNotes(MyNote note){

        SQLiteDatabase db = this.getWritableDatabase();

        //Similar to hashmap
        ContentValues values = new ContentValues();
        values.put(MyNote.TITLE_NAME, note.getTitle());
        values.put(MyNote.CONTENT_NAME, note.getContent());
        values.put(MyNote.DATE_NAME, System.currentTimeMillis());

        db.insert(MyNote.TABLE_NAME, null, values);

        //Log.v("NOTES SAVED", "YEAH");
        db.close();
    }

    public ArrayList<MyNote> getNotes(){
        //String selectQuery = "SELECT * FROM " + MyNote.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        //Similar to cursor in a cell of a table
        Cursor cursor = db.query(MyNote.TABLE_NAME, new String[]{MyNote.KEY_ID,
                                MyNote.TITLE_NAME, MyNote.CONTENT_NAME, MyNote.DATE_NAME},
                                null, null, null, null, MyNote.DATE_NAME + " DESC");

        //loop through the cursor
        if(cursor.moveToFirst()){
            //Todo: Try to change while loop is there's error
            while(!cursor.isLast()){

                MyNote note = new MyNote();
                String titleCol = cursor.getString(cursor.getColumnIndex(MyNote.TITLE_NAME));
                String contentCol = cursor.getString(cursor.getColumnIndex(MyNote.CONTENT_NAME));
                int itemId = cursor.getInt(cursor.getColumnIndex(MyNote.KEY_ID));
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(MyNote.DATE_NAME))).getTime());

                note.setTitle(titleCol);
                note.setContent(contentCol);
                note.setItemId(itemId);
                note.setRecordDate(date);
                noteList.add(note);
                cursor.moveToNext();
            }

        }
        db.close();

        return noteList;
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MyNote.TABLE_NAME, MyNote.KEY_ID + " = " + String.valueOf(id), null);
        db.close();
    }
}
