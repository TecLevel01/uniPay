package com.matt.unipay.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.matt.unipay.R;
import com.matt.unipay.util.Util;

public class AdminView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

    }

    public void openCourses(View view) {
        Util.loadActivity(this, Courses.class);
    }
}