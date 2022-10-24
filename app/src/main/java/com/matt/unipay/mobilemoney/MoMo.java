package com.matt.unipay.mobilemoney;

import static com.matt.unipay.util.Strings.enc_key;
import static com.matt.unipay.util.Strings.pub_key;

import android.app.Activity;
import android.content.Context;

import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.matt.unipay.R;

public class MoMo {
    Context context;
    private double price = 1000;
    private String fname = "Matthew";
    private String phone = "0773296451";
    private String email = "reypak.sweg@gmail.com";

    public void initMobileMoney(Context context) {

        new RaveUiManager((Activity) context).setAmount(price)
                .setCurrency(RaveConstants.UGX)
                .setEmail(email)
                .setfName(fname)
                .setlName(fname)
                .setPublicKey(pub_key)
                .setEncryptionKey(enc_key)
                .setPhoneNumber(phone)
                .acceptAccountPayments(true)
                .acceptBankTransferPayments(true)
                .acceptCardPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .setTxRef("UP")
                .withTheme(R.style.MyCustomTheme)
                .onStagingEnv(false)
                .showStagingLabel(false)
                .initialize();
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }*/
}
