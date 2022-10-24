package com.matt.unipay.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matt.unipay.R;
import com.matt.unipay.model.CourseItem;

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

    public static HashMap<String, Object> map() {
        return new HashMap<>();
    }


    public static void showCourseSheet(Context context, CourseItem courseItem) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.course_modal);

        EditText cname, tuition;
        Button btnSave, btnDel;

        cname = bottomSheetDialog.findViewById(R.id.cname);
        tuition = bottomSheetDialog.findViewById(R.id.tuition);
        btnSave = bottomSheetDialog.findViewById(R.id.btnSave);
        btnDel = bottomSheetDialog.findViewById(R.id.btnDel);

        if (courseItem != null) {
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
        bottomSheetDialog.show();
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

    public static class MyProgressDialog extends ProgressDialog {
        public MyProgressDialog(Context context, String msg) {
            super(context);
            getWindow().setBackgroundDrawableResource(R.drawable.rounded_all);
            setMessage(msg + "...");
            show();
        }
    }
}
