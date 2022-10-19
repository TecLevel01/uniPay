package com.matt.unipay.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matt.unipay.R;
import com.matt.unipay.mobilemoney.AirtelPay;
import com.matt.unipay.util.Util;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*MoMo moMo = new MoMo();
        moMo.initMobileMoney(this);*/

        AirtelPay airtelPay = new AirtelPay(this);
        airtelPay.initPay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Util.loadActivity(this, Login.class);
        finish();
    }

    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Util.loadActivity(this, Login.class);
            finish();
        } else {
            user.reload();
        }
    }
}