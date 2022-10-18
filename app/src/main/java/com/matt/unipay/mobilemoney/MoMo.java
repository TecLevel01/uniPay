package com.matt.unipay.mobilemoney;

import static com.matt.unipay.util.Strings.enc_key;
import static com.matt.unipay.util.Strings.pub_key;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.matt.unipay.R;

public class MoMo {
    private double price = 0;
    private String fname;
    private String phone;
    Context context;

    public void initMobileMoney(Context context) {

        new RaveUiManager((Activity) context).setAmount(price)
                .setCurrency(RaveConstants.UGX)
//                .setEmail(email)
                .setfName(fname)
                .setPublicKey(pub_key)
                .setEncryptionKey(enc_key)
                .setPhoneNumber(phone)
                .acceptUgMobileMoneyPayments(true)
                .setTxRef("MC-3243e")
                .withTheme(R.style.MyCustomTheme)
                .onStagingEnv(false)
                .initialize();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
//            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
//                Log.d("FlutterWave", message);
                Toast.makeText(context, "Transaction Successful", Toast.LENGTH_LONG).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
//                Log.d("FlutterWave", message);
                Toast.makeText(context, "ERROR! " + "Check connection and try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
