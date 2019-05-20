package com.example.hariharansivakumar.tike;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hariharan Sivakumar on 2/15/2018.
 */



public class SignUp_Fragment extends Fragment implements OnClickListener {


    private ProgressDialog progressDialog;
    public static JSONObject jsonObject=null;
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;

    private EditText editTextConfirmOtp;
    private AppCompatButton buttonConfirm;
    public RequestQueue requestQueue;
    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        progressDialog = new ProgressDialog(getActivity());

        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {


        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        location = (EditText) view.findViewById(R.id.location);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {

        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }



    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        boolean current;
        if((getLocation.length()==12)&&(getLocation.matches("^[0-9-]+$")))
        {
            current = true;
        }
        else
        {
            current = false;
        }

        if(current) {

            // Check if all strings are null or not
            if (getFullName.equals("") || getFullName.length() == 0
                    || getEmailId.equals("") || getEmailId.length() == 0
                    || getMobileNumber.equals("") || getMobileNumber.length() == 0
                    || getLocation.equals("") || getLocation.length() == 0
                    || getPassword.equals("") || getPassword.length() == 0
                    || getConfirmPassword.equals("")
                    || getConfirmPassword.length() == 0)

                new CustomToast().Show_Toast(getActivity(), view,
                        "All fields are required.");

                // Check if email id valid or not
            else if (!m.find())
                new CustomToast().Show_Toast(getActivity(), view,
                        "Your Email Id is Invalid.");

                // Check if both password should be equal
            else if (!getConfirmPassword.equals(getPassword))
                new CustomToast().Show_Toast(getActivity(), view,
                        "Both password doesn't match.");

                // Make sure user should check Terms and Conditions checkbox
            else if (!terms_conditions.isChecked())
                new CustomToast().Show_Toast(getActivity(), view,
                        "Please select Terms and Conditions.");

                // Else do signup or do your stuff
            else
                checkUser(getFullName,getEmailId,getPassword,getMobileNumber,getLocation);
        }
        else
        {

            Toast.makeText(getActivity(), "Error in Aadhar", Toast.LENGTH_SHORT)
                    .show();
        }

    }



    private void confirmOtp(final String name) throws JSONException {
        final String fname = name;
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(getContext().getApplicationContext());
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(getActivity(), "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if(response.equalsIgnoreCase("success")){
                                    //dismissing the progressbar
                                    loading.dismiss();

                                    //Starting a new activity
                                    startActivity(new Intent(getActivity(), Success.class));
                                    getActivity().finish();
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(getActivity(),"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp(fname);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Constants.KEY_OTP, otp);
                        params.put(Constants.KEY_USERNAME,fname);
                        return params;
                    }
                };

                //Adding the request to the queue
                RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest1);
            }
        });
    }










    private void checkUser(final String getFullName, final String getEmailId, final String getPassword, final String getMobileNumber, final String getLocation) {


        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Registering", "Please wait...", false, false);
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.getString("message")=="Success"){
                        //Asking user to confirm otp
                        Toast.makeText(getActivity(),"Some Problem",Toast.LENGTH_LONG).show();

                    }else{
                        //If not successful user may already have registered
                        confirmOtp(getFullName);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("name",getFullName);
                param.put("email",getEmailId);
                param.put("pass",getPassword);
                param.put("mobile",getMobileNumber);
                param.put("aadhar",getLocation);
                return param;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest2);
    }
}
