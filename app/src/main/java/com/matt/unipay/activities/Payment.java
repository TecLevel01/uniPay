package com.matt.unipay.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.matt.unipay.R;
import com.matt.unipay.util.Strings;
import com.matt.unipay.util.Util;

import java.util.Date;
import java.util.HashMap;

public class Payment extends AppCompatActivity {
    String year, sem;
    int amount;
    private AutoCompleteTextView yearAC, semAC;
    private FirebaseUser user;
    private EditText etAmount;
    private Dialog pinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        mInit();
    }

    private void mInit() {
        yearAC = Util.setYearAC(getWindow().getDecorView().getRootView());
        semAC = Util.setSemAC(getWindow().getDecorView().getRootView());
        etAmount = findViewById(R.id.etAmount);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openPinModal(View view) {
        year = yearAC.getText().toString();
        sem = semAC.getText().toString();
        String a = etAmount.getText().toString();

        if (!a.isEmpty()) {
            amount = Integer.parseInt(a);
        }

        if (!year.isEmpty() && !sem.isEmpty() && amount != 0) {
            pinModal();
        } else {
            Util.snackbar(this, "Fill all fields");
        }
    }

    private void pinModal() {
        pinDialog = Util.dialog(this, R.layout.pin_dialog);
        EditText etPhone = pinDialog.findViewById(R.id.phone),
                etPin = pinDialog.findViewById(R.id.etPin);
        Button btnConfirm = pinDialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(view1 -> {
            String phone = etPhone.getText().toString().trim(),
                    pin = etPin.getText().toString().trim();
            if (!phone.isEmpty() && !pin.isEmpty()) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    makePayment();
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makePayment() {
        pinDialog.dismiss();
        Util.MyProgressDialog dialog = new Util.MyProgressDialog(this, "Processing payment...");

        HashMap<String, Object> map = new HashMap<>();
        map.put("year", year);
        map.put("sem", sem);
        map.put("paid", amount);
        map.put("timestamp", new Date());

        Util.paymentsRef.document(user.getUid()).collection(Strings.sdata).add(map)
                .addOnSuccessListener(reference -> {
                    Util.userRef.document(user.getUid()).update("paid",  FieldValue.increment(amount)).addOnSuccessListener(runnable -> {
                        dialog.dismiss();
                        finish();
                    });
                });
    }
}