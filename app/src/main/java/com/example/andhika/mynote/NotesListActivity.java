package com.example.andhika.mynote;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.MyNote;

public class NotesListActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<MyNote> dbNotes = new ArrayList<>();
    private ListView listView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);

        listView = (ListView) findViewById(R.id.list);
        
        refreshData();
    }

    private void refreshData() {

        dbNotes.clear();
        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<MyNote> notesFromDB = dba.getNotes();

        for(int i = 0; i < notesFromDB.size(); ++i){
            String title = notesFromDB.get(i).getTitle();
            String dateText = notesFromDB.get(i).getRecordDate();
            String content = notesFromDB.get(i).getContent();
            int mid = notesFromDB.get(i).getItemId();

            MyNote note = new MyNote();
            note.setTitle(title);
            note.setContent(content);
            note.setRecordDate(dateText);
            note.setItemId(mid);

            dbNotes.add(note);
        }
        dba.close();

        //setup adapter
        noteAdapter = new NoteAdapter(NotesListActivity.this, R.layout.note_row, dbNotes);
        listView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }

    public class NoteAdapter extends ArrayAdapter<MyNote>{

        Activity activity;
        int layoutResource;
        MyNote myNote;
        ArrayList<MyNote> data = new ArrayList<>();

        public NoteAdapter(Activity activityIn, int resource, ArrayList<MyNote> dataIn) {
            super(activityIn, resource, dataIn);
            activity = activityIn;
            layoutResource = resource;
            data = dataIn;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Nullable
        @Override
        public MyNote getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getPosition(MyNote item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //This part makes the program more efficient
            //It recycle the row
            View row = convertView;
            ViewHolder holder = null;
            //Checks if there's no row that has been created, we have to create that row
            if(row == null || (row.getTag()) == null){
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResource, null);
                holder = new ViewHolder();

                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);

                row.setTag(holder);
            }
            else {
                holder = (ViewHolder) row.getTag();
            }

            //this stores the row of the note that has been tapped
            holder.mNote = getItem(position);
            holder.mTitle.setText(holder.mNote.getTitle());
            holder.mDate.setText(holder.mNote.getRecordDate());

            final ViewHolder finalHolder = holder;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = finalHolder.mNote.getContent().toString();
                    String dateText = finalHolder.mNote.getRecordDate().toString();
                    String title = finalHolder.mNote.getTitle().toString();

                    int mId = finalHolder.mNote.getItemId();

                    Intent intent = new Intent(NotesListActivity.this, NoteDetailActivity.class);
                    intent.putExtra("content", text);
                    intent.putExtra("date", dateText);
                    intent.putExtra("title", title);
                    intent.putExtra("id", mId);
                    startActivity(intent);
                }
            });
            return row;
        }
    }

    //Class to assist recycling of rows
    private class ViewHolder{
        MyNote mNote;
        TextView mTitle;
        TextView mId;
        TextView mContent;
        TextView mDate;
    }
}
