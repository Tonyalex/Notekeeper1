package com.example.notekeeper1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.example.notekeeper1.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mCanceling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if (mViewModel.mIsNewlyCreated && savedInstanceState != null)
            mViewModel.restoreState(savedInstanceState);
        mViewModel.mIsNewlyCreated = false;

        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        mTextNoteTitle = findViewById(R.id.Note_text_title);
        mTextNoteText = findViewById(R.id.Note_text_note);
        if (!mIsNewNote)
            DisplayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);


    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)
            return;
       mViewModel.mOriginalCourseValue = mNote.getCourse().getCourseId();
        mViewModel.mOriginalTitleValue = mNote.getTitle();
        mViewModel.mOriginalTextValue = mNote.getText();

    }

    private void DisplayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent Intent = getIntent();
        int position = Intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if (mIsNewNote) {
            createNewNote();
        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCanceling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            }else {
                storePreviousValues();
            }
            } else{
                saveNote();
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            mViewModel.saveState(outState);
    }

    private void storePreviousValues() {
        CourseInfo courses = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseValue);
        mNote.setCourse(courses);
        mNote.setTitle(mViewModel.mOriginalTitleValue);
        mNote.setText(mViewModel.mOriginalTextValue);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem() );
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            SendEmail();
            return true;

        } else if (id == R.id.Action_Cancel) {

            mCanceling = true;
            finish();
            }

        return super.onOptionsItemSelected(item);
    }

    private void SendEmail() {
        CourseInfo courses = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "checkout what i learnt in  pluralsight courses \' " + courses.getTitle() + " \' " + mTextNoteText.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(intent.EXTRA_SUBJECT, subject);
        intent.putExtra(intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}