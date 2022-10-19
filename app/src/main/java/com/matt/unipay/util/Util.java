package com.matt.unipay.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matt.unipay.R;

public class Util {
    public static CollectionReference userRef = FirebaseFirestore.getInstance().collection(Strings.susers);

    public static void loadActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
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
