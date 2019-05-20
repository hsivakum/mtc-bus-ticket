package com.example.hariharansivakumar.tike;

import android.content.Intent;
import android.icu.util.IndianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {

    Spinner spinner,spinner2,spinner3,spinner4;
    public static String strDate="";
    public static JSONObject jsonObject3;
    public static String datee = "";
    public static String amount ="";
    public static String status;
    public static String statStr ="";
    public static String heads= "";
    static List<Hist> GetDataAdapter1;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        button = (Button)findViewById(R.id.btnGetMoreResults);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                amount = spinner.getSelectedItem().toString();
                datee = spinner2.getSelectedItem().toString();
                status = spinner3.getSelectedItem().toString();
                heads = spinner4.getSelectedItem().toString();
                if(datee.trim().equals("Yesterday"))
                {
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH);
                    IndianCalendar gc = new IndianCalendar();
                    gc.add(Calendar.DATE, -1);
                    strDate = mdformat.format(gc.getTime());
                    Toast.makeText(getApplicationContext(),strDate,Toast.LENGTH_SHORT).show();
                }
                else if(datee.trim().equals("Today"))
                {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd ",Locale.ENGLISH);
                    strDate = mdformat.format(calendar.getTime());
                    Toast.makeText(getApplicationContext(),strDate,Toast.LENGTH_SHORT).show();
                }
                else if(datee.trim().equals("Tomorrow"))
                {
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd ",Locale.ENGLISH);
                    IndianCalendar gc = new IndianCalendar();
                    gc.add(Calendar.DATE, 1);
                    strDate = mdformat.format(gc.getTime());
                    Toast.makeText(getApplicationContext(),strDate,Toast.LENGTH_SHORT).show();
                }
                else if(datee.trim().equals("No Date"))
                {
                    datee="";
                }


                if(status.trim().equals("Booked"))
                {
                    statStr = "s";
                }
                else if(status.trim().equals("Generated"))
                {
                    statStr ="n";
                }
                else if(status.trim().equals("No Filter"))
                {
                    statStr="";
                }

                if(amount.trim().equals("No Filter"))
                {
                    amount="";
                }

                if(heads.trim().equals("No Filter"))
                {
                    heads="";
                }

                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                getHist(user.getMobile());
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","Hello");
                setResult(2,intent);
                finish();
            }
        });


        spinner = (Spinner)findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        spinner4 = (Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.amount));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.datees));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter1);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.statuss));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(arrayAdapter2);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.heads));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(arrayAdapter3);

    }


    public void getHist(final long mobi)
    {
        final String s = Long.toString(mobi);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_HIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject3 = new JSONObject(response);
                    Log.e("msg",jsonObject3.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"error VOLLEY "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("mobile",s);
                params.put("datee",strDate);
                params.put("amount",amount);
                params.put("status",statStr);
                params.put("noh",heads);
                return params;
            }
        };

        RequestHandler.getInstance(getApplication()).addToRequestQueue(stringRequest);


    }



    public static List<Hist> check() {

        JSONObject json;
        GetDataAdapter1 = new ArrayList<>();
        if(jsonObject3==null)
        {
            Hist GetDataAdapter2 = new Hist();
            String s = "Null"+ "---->" + "Null";

            GetDataAdapter2.setStop(s);
            GetDataAdapter2.setAmount("0");
            GetDataAdapter2.setFrom("Null");
            GetDataAdapter2.setTo("Null");
            GetDataAdapter2.setDates("9999-12-31");
            GetDataAdapter2.setTicketid("12jnM");
            GetDataAdapter2.setHeads("0");

            GetDataAdapter1.add(GetDataAdapter2);
        }
        else
        {
            JSONArray cast = jsonObject3.optJSONArray("hist");
            for (int i=0; i<cast.length(); i++) {
                Hist GetDataAdapter2 = new Hist();
                json = cast.optJSONObject(i);
                String s = json.optString("from") + "---->" + json.optString("to");

                GetDataAdapter2.setStop(s);
                GetDataAdapter2.setAmount(json.optString("amount"));
                GetDataAdapter2.setFrom(json.optString("from"));
                GetDataAdapter2.setTo(json.optString("to"));
                GetDataAdapter2.setDates(json.optString("date"));
                GetDataAdapter2.setTicketid(json.optString("token"));
                GetDataAdapter2.setHeads(json.optString("heads"));

                GetDataAdapter1.add(GetDataAdapter2);

            }
        }



        return GetDataAdapter1;
    }
}
