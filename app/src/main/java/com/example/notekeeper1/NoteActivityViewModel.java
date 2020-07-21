package com.example.notekeeper1;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper1.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper1.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper1.ORIGINAL_NOTE_TEXT";

    public String mOriginalCourseValue;
    public String mOriginalTitleValue;
    public String mOriginalTextValue;
    public boolean mIsNewlyCreated = true;


    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalCourseValue);
        outState.putString(ORIGINAL_NOTE_TITLE,mOriginalTitleValue);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalTextValue);

    }
    public void restoreState(Bundle inState){
        mOriginalCourseValue = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalTitleValue = inState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalTextValue = inState.getString(ORIGINAL_NOTE_TEXT);

    }

}
