package com.matt.unipay.mobilemoney;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AirtelPay {
    Context context;
    String transactionsURL = "https://openapiuat.airtel.africa/merchant/v1/payments/";

    public AirtelPay(Context context) {
        this.context = context;
    }

    public void initPay() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, transactionsURL, response -> {
            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
        }, error -> {
            Log.i("56y", error.toString());
            if (error.getMessage() != null) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", "name");
                params.put("job", "job");

                return params;
            }
        };
        // add to queue
        queue.add(request);

       /* URL obj = new URL("/openapiuat.airtel.africa/merchant/v1/payments/");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.i("56y", response.toString());*/
    }

    private JSONObject getJsonObject(String name, String age) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("name", name);
        userJson.put("age", age);
        jsonObject.put("user", userJson);
        return jsonObject;
    }

   /* And add to your Volley JSON request Object
        try{
                JSONObject requestObject =new JSONObject();
                requestObject.put("yourParamName",getJsonObject("martin","20"));
            }catch(JSONException e){

            }*/
}
