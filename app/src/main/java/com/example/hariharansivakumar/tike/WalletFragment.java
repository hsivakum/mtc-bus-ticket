package com.example.hariharansivakumar.tike;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {


    Button button;
    private ProgressDialog progressDialog;
    public static JSONObject jsonObject;
    TextView textView,addwallet;
    public String s=null;

    public static WalletFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        WalletFragment firstFragment = new WalletFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);
        button = (Button)view.findViewById(R.id.showbalance);
        textView = (TextView)view.findViewById(R.id.balance);
        addwallet = (TextView)view.findViewById(R.id.addmoney);
        progressDialog = new ProgressDialog(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        s =  Long.toString(user.getMobile());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GETBALANCE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error"))
                            {
                                progressDialog.dismiss();
                                textView.setText("");
                                textView.setText(jsonObject.getString("message"));
                            }
                            else
                            {
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("mobile",s);
                        return params;
                    }
                };

                RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        });

        addwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMoney.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
