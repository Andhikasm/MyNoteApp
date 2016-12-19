package com.example.andhika.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;

public class NoteDetailActivity extends AppCompatActivity {

    private TextView title, date, content;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        title = (TextView) findViewById(R.id.detailsTitle);
        date = (TextView) findViewById(R.id.detailsDateText);
        content = (TextView) findViewById(R.id.detailsTextView);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title.setText(bundle.getString("title"));
            date.setText("Created: " + bundle.getString("date"));
            content.setText(bundle.getString("content"));
            final int id = bundle.getInt("id");

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteNote(id);

                    Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_LONG).show();
                    if(dba.getNotes().size() == 0){
                        startActivity(new Intent(NoteDetailActivity.this, MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(NoteDetailActivity.this, DisplayNotesActivity.class));
                    }

                }
            });
        }
    }
}
