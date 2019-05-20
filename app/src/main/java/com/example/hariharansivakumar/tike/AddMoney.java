package com.example.hariharansivakumar.tike;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMoney extends AppCompatActivity implements PaymentResultListener{
    private static final String TAG = AddMoney.class.getSimpleName();

    EditText textViewPrice;

    public int amounts,tot;
    public static String finalamount;
    public static String email;
    public static String phone;
    private ProgressDialog progressDialog;
    public static JSONObject jsonObject;
    public String strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        strDate = mdformat.format(calendar.getTime());
        textViewPrice = findViewById(R.id.addmoney1);
        progressDialog = new ProgressDialog(getApplicationContext());
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        email = user.getEmail();
        phone = Long.toString(user.getMobile());

        Checkout.preload(getApplicationContext());
        //attaching a click listener to the button buy
        findViewById(R.id.getGate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        amounts = Integer.parseInt(textViewPrice.getText().toString());
        tot = amounts*100;
        finalamount = Integer.toString(tot);
        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "E-MTC");
            options.put("description", "Wallet Adding");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", finalamount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        try{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SETBALANCE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean("error"))
                        {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
            ){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("mobile",phone);
                    params.put("txtamount",textViewPrice.getText().toString());
                    params.put("txtid",razorpayPaymentID);
                    params.put("dates",strDate);
                    return params;
                }
            };

            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }  catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    //private void setBalance(final String razorpayPaymentID) {


   // }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Log.e("error",response);
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}
