package com.example.notekeeper1;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Note_list_Activity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Note_list_Activity.this,NoteActivity.class));
            }
        });
        initializeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAdapterNotes.notifyDataSetChanged();
    }

    private void initializeDisplayContent () {
            final ListView noteListView = findViewById(R.id.note_list_view);
            List<NoteInfo> notes = DataManager.getInstance().getNotes();
            mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
            noteListView.setAdapter(mAdapterNotes);


            noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Note_list_Activity.this, NoteActivity.class);
                    NoteInfo note = (NoteInfo)  noteListView.getItemAtPosition(position);
                    intent.putExtra(NoteActivity.NOTE_POSITION,position);

                    startActivity(intent);
                }
            });

                }

        }




