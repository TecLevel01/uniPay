package com.matt.unipay.activities;

import static com.matt.unipay.util.Util.PriceFormat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matt.unipay.R;
import com.matt.unipay.adapters.DashboardAdapter;
import com.matt.unipay.mobilemoney.MoMo;
import com.matt.unipay.model.DashboardItem;
import com.matt.unipay.model.UserItem;
import com.matt.unipay.util.Util;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    String scourse, syear, ssem;
    private MoMo moMo;
    private RecyclerView recView1, recView2;
    private ArrayList<DashboardItem> items1, items2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        mInit();
        new Handler().postDelayed(() -> {
            getData();
        }, 1);

        /*Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pin_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();*/

      /*  moMo = new MoMo();
        moMo.initMobileMoney(this); */

       /* AirtelPay airtelPay = new AirtelPay(this);
        airtelPay.initPay2();*/
    }

    private void getData() {
        items1.add(new DashboardItem("Tuition", PriceFormat(900000)));
        items1.add(new DashboardItem("Paid Amount", PriceFormat(600000)));
        items1.add(new DashboardItem("Balance", PriceFormat(300000)));


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Util.userRef.document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                UserItem userItem = documentSnapshot.toObject(UserItem.class);
                items2.add(new DashboardItem("Year of Study", userItem.getYear()));
                items2.add(new DashboardItem("Semester", userItem.getSem()));
                items2.add(new DashboardItem("Course", userItem.getCourse()));

                DashboardAdapter adapter2 = new DashboardAdapter(this, items2, R.drawable.ic_outline_library_books_24);
                recView2.setAdapter(adapter2);
            });
        }

        DashboardAdapter adapter1 = new DashboardAdapter(this, items1, R.drawable.ic_baseline_attach_money_24);

        recView1.setAdapter(adapter1);
    }

    private void mInit() {
        recView1 = findViewById(R.id.recView1);
        recView2 = findViewById(R.id.recView2);
        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 2, Menu.NONE, "Admin Mode");
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            logout();
        } else if (item.getItemId() == 2) {
            Util.loadActivity(this, AdminView.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openPay(View view) {
        Util.loadActivity(this, Payment.class);
    }
}