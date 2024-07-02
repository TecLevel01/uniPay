package com.oli.unipay.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.oli.unipay.R;
import com.oli.unipay.util.Util;
import com.oli.unipay.util.Util.MyProgressDialog;

public class Login extends AppCompatActivity {


    private EditText etEmail, etPwrd;
    private String pwrd, email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mInit();

    }

    private void mInit() {
        etEmail = findViewById(R.id.email);
        etPwrd = findViewById(R.id.pwrd);
        auth = FirebaseAuth.getInstance();
    }

    public void openRegister(View view) {
        Util.loadActivity(this, Register.class);
        finish();
    }

    public void startLogin(View view) {
        validateData();
    }

    private void validateData() {
        email = etEmail.getText().toString().trim();
        pwrd = etPwrd.getText().toString().trim();
        if (!email.isEmpty() && !pwrd.isEmpty()) {
            LoginUser();
        }
    }

    private void LoginUser() {
        MyProgressDialog dialog = new MyProgressDialog(this, "Logging in");

        auth.signInWithEmailAndPassword(email, pwrd).addOnCompleteListener(task -> {
            String msg;
            if (task.isSuccessful()) {

                finalExecute();

                msg = "Logged In";
            } else {
                msg = task.getException().getMessage();
                // If sign in fails, display a message to the user.
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void finalExecute() {
        finish();
        Util.loadActivity(this, Dashboard.class);
    }
}