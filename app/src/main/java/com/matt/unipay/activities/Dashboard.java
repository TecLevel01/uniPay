package com.matt.unipay.activities;

import static com.matt.unipay.util.Util.PriceFormat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.matt.unipay.R;
import com.matt.unipay.adapters.DashboardAdapter;
import com.matt.unipay.classes.Reports;
import com.matt.unipay.model.DashboardItem;
import com.matt.unipay.model.UserItem;
import com.matt.unipay.util.Util;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private RecyclerView recView1, recView2;
    private ArrayList<DashboardItem> items1, items2;
    private FirebaseUser user;
    private DashboardAdapter adapter1, adapter2;
    private int tuition;
    private boolean getCourse;
    private TextView tvName, tvReg;
    private View v2, v1;
    private UserItem userItem;

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

       /* moMo = new MoMo();
        moMo.initMobileMoney(this);*/

       /* AirtelPay airtelPay = new AirtelPay(this);
        airtelPay.initPay2();*/
    }

    private void getData() {


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Util.userRef.document(user.getUid()).addSnapshotListener((value, error) -> {
                if (error == null) {
                    if (value.exists()) {
                        clearList();

                        userItem = value.toObject(UserItem.class);
                        tvName.setText(String.format("%s %s", userItem.getFname(), userItem.getLname()));
                        tvReg.setText(userItem.getRegno());

                        // run for the first time
                        if (!getCourse) {
                            Query query = Util.courseRef.whereEqualTo("name", userItem.getCourse());
                            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    tuition = snapshot.getDouble("tuition").intValue();

                                    setFinancials(tuition, userItem.getPaid());
                                    setDetails(userItem);

                                    getCourse = true;
                                }
                            });
                        } else {
                            setFinancials(tuition, userItem.getPaid());
                            setDetails(userItem);
                        }

                    }
                }
            });

            adapter2 = new DashboardAdapter(this, items2, R.drawable.ic_outline_library_books_24);
            recView2.setAdapter(adapter2);

            adapter1 = new DashboardAdapter(this, items1, R.drawable.ic_baseline_attach_money_24);
            recView1.setAdapter(adapter1);
        }

    }

    private void setDetails(UserItem userItem) {
        items2.add(new DashboardItem("Course", userItem.getCourse()));
        items2.add(new DashboardItem("Year of Study", userItem.getYear()));
        items2.add(new DashboardItem("Semester", userItem.getSem()));
        Util.hideProgress(v2);

        adapter2.notifyDataSetChanged();
    }

    private void setFinancials(int tuition, int paid) {
        Util.hideProgress(v1);
        items1.add(new DashboardItem("Tuition", PriceFormat(tuition)));
        items1.add(new DashboardItem("Paid Amount", PriceFormat(paid)));
        // set balance
        items1.add(new DashboardItem("Balance", PriceFormat(tuition - paid)));
        adapter1.notifyDataSetChanged();
    }

    private void clearList() {
        items1.clear();
        items2.clear();
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    private void mInit() {
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        recView1 = v1.findViewById(R.id.recView);
        recView2 = v2.findViewById(R.id.recView);
        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
        tvName = findViewById(R.id.tvName);
        tvReg = findViewById(R.id.tvReg);
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

    public void openTransactions(View view) {
        Util.loadActivity(this, Transactions.class);
    }

    public void openStudentDetails(View view) {
        Util.showStudentSheet(this, userItem, tuition);
    }

    public void openReports(View view) {
        Reports reports = new Reports(tuition, userItem, this, user);
        reports.loadLedger();
    }
}