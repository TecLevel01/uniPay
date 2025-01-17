package com.oli.unipay.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.oli.unipay.R;
import com.oli.unipay.model.CourseItem;
import com.oli.unipay.model.UserItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Util {
    public static CollectionReference userRef = FirebaseFirestore.getInstance().collection(Strings.susers);
    public static CollectionReference courseRef = FirebaseFirestore.getInstance().collection(Strings.scourse);
    public static CollectionReference paymentsRef = FirebaseFirestore.getInstance().collection(Strings.spayments);

    public static void loadActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static Snackbar snackbar(Context context, String s) {
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_LONG).setAction("OK", v -> {
        });
        snackbar.show();
        return snackbar;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd MMM yy • HH:mm");
        return formatter.format(date);
    }

    public static String dateFormat2(Date date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MMM-yy");
        return formatter.format(date);
    }


    public static void showCourseSheet(Context context, CourseItem courseItem) {
        BottomSheetDialog bottomSheetDialog = MyBottomSheetDialog(context, R.layout.course_modal);

        EditText cname, tuition;
        Button btnSave, btnDel;
        cname = bottomSheetDialog.findViewById(R.id.cname);
        tuition = bottomSheetDialog.findViewById(R.id.tuition);
        btnSave = bottomSheetDialog.findViewById(R.id.btnSave);
        btnDel = bottomSheetDialog.findViewById(R.id.btnDel);

        if (courseItem != null) {
            cname.setFocusable(false);
            cname.setText(courseItem.getName());
            tuition.setText(String.valueOf(courseItem.tuition));
        } else {
            btnDel.setVisibility(View.GONE);
        }

        btnDel.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            snackbar(context, "Confirm Delete").setAction("YES", view1 -> {
                Util.courseRef.document(courseItem.getCid()).delete().addOnSuccessListener(runnable -> {
                    snackbar(context, "Deleted");
                });
            });
        });

        btnSave.setOnClickListener(view -> {
            String scname = cname.getText().toString().trim();
            String stuition = tuition.getText().toString();

            if (!scname.isEmpty() && !stuition.isEmpty()) {

                int ituition = Integer.valueOf(stuition);
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", scname);
                map.put("tuition", ituition);

                if (courseItem != null) {
                    Util.courseRef.document(courseItem.getCid()).update(map).addOnSuccessListener(runnable -> {
                        snackbar(context, "Update Successful");
                    });
                } else {
                    Util.courseRef.add(map).addOnSuccessListener(runnable -> {
                        snackbar(context, "Added Successful");
                    });
                }
                bottomSheetDialog.hide();
            }
        });
    }

    public static BottomSheetDialog MyBottomSheetDialog(Context context, int layoutRes) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(layoutRes);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }

    public static void showBankSheet(Context context, Runnable runnable) {
        BottomSheetDialog bottomSheetDialog = MyBottomSheetDialog(context, R.layout.bank_modal);
        EditText cno = bottomSheetDialog.findViewById(R.id.cno);
        Button btnPay = bottomSheetDialog.findViewById(R.id.btnPay);
        btnPay.setOnClickListener(view -> {
            if (cno != null) {
                runnable.run();
            }
        });
    }

    public static void showStudentSheet(Context context, UserItem userItem, int tuition, int status) {
        BottomSheetDialog bottomSheetDialog = MyBottomSheetDialog(context, R.layout.student_details);

        EditText etEmail = bottomSheetDialog.findViewById(R.id.email),
                gender = bottomSheetDialog.findViewById(R.id.gender),
                courseAC = bottomSheetDialog.findViewById(R.id.course),
//                yearAc = bottomSheetDialog.findViewById(R.id.year),
//                semAc = bottomSheetDialog.findViewById(R.id.sem),
                etFname = bottomSheetDialog.findViewById(R.id.fname),
                etLname = bottomSheetDialog.findViewById(R.id.lname),
                etPaid = bottomSheetDialog.findViewById(R.id.paid),
                etbalance = bottomSheetDialog.findViewById(R.id.balance),
                etRegNo = bottomSheetDialog.findViewById(R.id.regno);

        AutoCompleteTextView yearAc = bottomSheetDialog.findViewById(R.id.year),
                semAc = bottomSheetDialog.findViewById(R.id.sem);

        semAc.setText(userItem.getSem());
        yearAc.setText(userItem.getYear());

        if (status != 0) {
            yearAc = Util.setYearAC(bottomSheetDialog.getWindow().getDecorView());
            semAc = Util.setSemAC(bottomSheetDialog.getWindow().getDecorView());
        }


        View v1 = bottomSheetDialog.findViewById(R.id.v1),
                v2 = bottomSheetDialog.findViewById(R.id.btnSave);

        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);

        gender.setText(userItem.getGender());
        courseAC.setText(userItem.getCourse());
        etPaid.setText(PriceFormat(userItem.getPaid()));
        etRegNo.setText(userItem.getRegno());
        etLname.setText(userItem.getLname());
        etFname.setText(userItem.getFname());
        etbalance.setText(PriceFormat(tuition - userItem.getPaid()));

        etRegNo.setFocusable(false);
        etbalance.setFocusable(false);
        etPaid.setFocusable(false);

        AutoCompleteTextView finalSemAc = semAc;
        AutoCompleteTextView finalYearAc = yearAc;


        v2.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String fname = etFname.getText().toString().trim(),
                    lname = etLname.getText().toString().trim(),
                    year = finalYearAc.getText().toString(),
                    sem = finalSemAc.getText().toString();
            if (!fname.isEmpty() && !lname.isEmpty())
                if (user != null) {
                    MyProgressDialog d = new MyProgressDialog(context, "Updating");

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("fname", fname);
                    map.put("lname", lname);

                    if (status != 0) {
                        map.put("year", year);
                        map.put("sem", sem);
                    }

                    String uid = userItem.getUid();
                    if (userItem.getUid() == null) {
                        uid = user.getUid();
                    }

                    Util.userRef.document(uid).set(map, SetOptions.merge()).addOnSuccessListener(runnable -> {
                        snackbar(context, "Updated Successfully");
                        d.dismiss();
                    });
                    bottomSheetDialog.dismiss();
                }
        });

    }

    public static void SetSharedPrefs(Context context, String key, String s) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, s).apply();
    }

    public static String GetSharedPrefs(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static void hideProgress(View view) {
        ProgressBar progressBar = view.findViewById(R.id.progBar);
        progressBar.setVisibility(View.GONE);
    }

    public static String PriceFormat(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String ugxprice = decimalFormat.format(amount);
        return "UGX " + ugxprice;
    }

    public static AutoCompleteTextView setYearAC(View view) {
        AutoCompleteTextView yearAC = view.findViewById(R.id.year);
        ArrayList<String> year = new ArrayList<>();
        String y = "Year";
        year.add(y + " 1");
        year.add(y + " 2");
        year.add(y + " 3");
        year.add(y + " 4");
        yearAC.setAdapter(Util.getACAdapter(year, view));
        return yearAC;
    }

    public static AutoCompleteTextView setSemAC(View view) {
        AutoCompleteTextView myAC = view.findViewById(R.id.sem);
        ArrayList<String> sem = new ArrayList<>();
        String s = "Sem";
        sem.add(s + " 1");
        sem.add(s + " 2");
        myAC.setAdapter(Util.getACAdapter(sem, view));
        return myAC;
    }

    private static ArrayAdapter<String> getACAdapter(ArrayList<String> year, View view) {
        return new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, year);
    }

    public static Dialog dialog(Context context, int layoutID) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(layoutID);
        dialog.show();
        return dialog;
    }

    public static boolean onOptionsItemSelected(Context context, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((Activity) context).finish();
        }
        return false;
    }

    public static class MyProgressDialog extends ProgressDialog {
        public MyProgressDialog(Context context, String msg) {
            super(context);
            getWindow().setBackgroundDrawableResource(R.drawable.rounded_all);
            setMessage(msg + "...");
            show();
        }
    }
}
