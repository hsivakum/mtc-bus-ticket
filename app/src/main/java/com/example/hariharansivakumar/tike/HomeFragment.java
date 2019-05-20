package com.example.hariharansivakumar.tike;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static JSONObject jsonObject=null;
    public static JSONObject fromlogin =null;
    TextView textView1,textView2;
    static String res="a";
    static String tok ="b";
    Login_Fragment login_fragment;
    private ProgressDialog progressDialog;
    public static JSONObject jsonObject1;
    public static  String fromst;
    public static String tost;
    public static String head;
    EditText editText;

    private Bitmap bitmap;

    public static HomeFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        HomeFragment firstFragment = new HomeFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getActivity());
        getStop();

        editText = (EditText)view.findViewById(R.id.headcount);
        fromlogin = login_fragment.jsonObject;



        textView1 = (TextView)view.findViewById(R.id.fromstop);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Main3Activity.class);
                startActivityForResult(intent,1);
            }
        });

        textView2 = (TextView)view.findViewById(R.id.tostop);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Main3Activity.class);
                startActivityForResult(intent,2);
            }
        });

        Button b =(Button)view.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromst = textView1.getText().toString();
                tost = textView2.getText().toString();
                if(fromst == "Source" && tost =="Destination")
                {
                    Toast.makeText(getActivity(),"Select Source and Destination",Toast.LENGTH_SHORT).show();
                }
                else if(fromst== "Source" || tost =="Destination")
                {
                    Toast.makeText(getActivity(),"Select Source or Destination",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHECK, new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                jsonObject1 = new JSONObject(response);
                                if(!jsonObject1.getBoolean("error"))
                                {
                                    head = editText.getText().toString();
                                    long mob =  fromlogin.getLong("mobile");
                                    bookTicket(fromst,tost,mob,head);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),jsonObject1.getString("message"),Toast.LENGTH_LONG).show();

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
                            params.put("from",fromst);
                            params.put("to",tost);
                            return params;
                        }
                    };

                    RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
                }

            }
        });

        return  view;
    }



    public void bookTicket(final String fromst, final String tost, final long mob, final String head)
    {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        final String saltStr = salt.toString();
        final String mobi = Long.toString(mob);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_BOOK, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject2 = new JSONObject(response);
                    if(!jsonObject2.getBoolean("error"))
                    {
                        final String idf = jsonObject2.getString("token");
                        final String fromstr = jsonObject2.getString("from");
                        final String tostr = jsonObject2.getString("to");
                        final String datess1 = jsonObject2.getString("dates");
                        final String amounts1 =jsonObject2.getString("amount");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                        alertDialogBuilder.setTitle("Welcome");
                        alertDialogBuilder.setMessage(jsonObject2.getString("message"));
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id){
                                        Intent intent = new Intent(getActivity(),ShowTicket.class);
                                        intent.putExtra("id",idf);
                                        intent.putExtra("from",fromstr);
                                        intent.putExtra("to",tostr);
                                        intent.putExtra("date",datess1);
                                        intent.putExtra("amount",amounts1);
                                        startActivityForResult(intent,10);
                                        Toast.makeText(getActivity(),"Thank You",Toast.LENGTH_LONG).show();
                                    }});
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Toast.makeText(getActivity(),jsonObject2.getString("message"),Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                        alertDialogBuilder.setTitle("Try again");
                        alertDialogBuilder.setMessage(jsonObject2.getString("message"));
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id){
                                Toast.makeText(getActivity(),"Try Again",Toast.LENGTH_LONG).show();
                        }});
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


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
                params.put("from", fromst);
                params.put("to", tost);
                params.put("token",saltStr);
                params.put("mobile",mobi);
                params.put("head",head);

                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest1);




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&& resultCode== RESULT_OK)
        {
            String revieved = data.getStringExtra("Data");
            res = revieved;
            textView1.setText(revieved);
        }
        else if(requestCode==2 && resultCode==RESULT_OK)
        {
            String tk = data.getStringExtra("Data");
            tok = tk;
            textView2.setText(tk);
        }
        else if(requestCode==10 && resultCode==RESULT_OK)
        {
            textView1.setText("Select Source");
            textView2.setText("Select Destination");
            editText.setText(null);
        }
    }




    public  void getStop() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_STOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    jsonObject= new JSONObject(response);
                    if(!jsonObject.getBoolean("error"))
                    {

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

                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key","hello");
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


}
