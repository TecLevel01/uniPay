package com.matt.unipay.activities;

import static com.matt.unipay.util.Util.userRef;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.matt.unipay.R;
import com.matt.unipay.util.Util;
import com.matt.unipay.util.Util.MyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    String fname, lname, course, regno, gender, email, pwrd, year, sem;
    private AutoCompleteTextView autoCompleteTextView, yearAC, semAC, courseAC;
    private EditText etEmail, etPwrd, etFname, etLname, etRegNo;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        mInit();
        setACs();
        setCourse();
    }

    private void setCourse() {
        ArrayList<String> course = new ArrayList<>();
        Util.courseRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot snapshot : task.getResult()) {
                    String n = snapshot.getString("name");
                    course.add(n);
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
            courseAC.setAdapter(adapter);
        });
    }

    private void setACs() {
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gender);
        yearAC = Util.setYearAC(getWindow().getDecorView().getRootView());
        semAC = Util.setSemAC(getWindow().getDecorView().getRootView());

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

    }

    private void mInit() {
        autoCompleteTextView = findViewById(R.id.list1);
        courseAC = findViewById(R.id.course);
        etEmail = findViewById(R.id.email);
        etPwrd = findViewById(R.id.pwrd);
        etFname = findViewById(R.id.fname);
        etLname = findViewById(R.id.lname);
        etRegNo = findViewById(R.id.regno);

        auth = FirebaseAuth.getInstance();
    }


    public void openLogin(View view) {
        Util.loadActivity(this, Login.class);
        finish();
    }

    public void startRegister(View view) {
        validateData();
    }

    private void validateData() {
        email = etEmail.getText().toString().trim();
        fname = etFname.getText().toString().trim();
        lname = etLname.getText().toString().trim();
        pwrd = etPwrd.getText().toString().trim();
        regno = etRegNo.getText().toString().trim();
        course = courseAC.getText().toString().trim();
        gender = autoCompleteTextView.getText().toString();
        year = yearAC.getText().toString();
        sem = semAC.getText().toString();

        if (!email.isEmpty() && !pwrd.isEmpty() && !regno.isEmpty()) {
            RegisterUser();
        } else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void RegisterUser() {

        MyProgressDialog dialog = new MyProgressDialog(this, "Signing up");

        auth.createUserWithEmailAndPassword(email, pwrd).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                MyProgressDialog dialog2 = new MyProgressDialog(this, "Logging in");

                FirebaseUser user = auth.getCurrentUser();
                HashMap<String, Object> userdata = new HashMap<>();
                userdata.put("fname", fname);
                userdata.put("lname", lname);
                userdata.put("email", user.getEmail());
                userdata.put("pwrd", pwrd);
                userdata.put("course", course);
                userdata.put("gender", gender);
                userdata.put("regno", regno);
                userdata.put("year", year);
                userdata.put("sem", sem);

                // sending data
                userRef.document(user.getUid()).set(userdata).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

//                        updateProfile(user, uname);
                        Util.loadActivity(this, Dashboard.class);
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                });
            } else {
                String msg = task.getException().getMessage();
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

    }
}