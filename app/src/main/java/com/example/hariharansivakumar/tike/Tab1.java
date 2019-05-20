package com.example.hariharansivakumar.tike;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hariharan Sivakumar on 2/24/2018.
 */

public class Tab1 extends Fragment {

    static  ArrayList<String> k;
    String[] items;
    static ArrayList<String> listItems ;
    ArrayAdapter<String> adapter;
    ListView listView;
    HomeFragment homeFragment;
    SearchView searchView;
    String s= null;
    static JSONObject jsonObject1 = null;
    static String geto = null;
    static String  getfro = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1,container,false);


        jsonObject1=homeFragment.jsonObject;
        try {
            if(!jsonObject1.getBoolean("error")) {
                s= jsonObject1.getString("stops");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = (ListView)view.findViewById(R.id.listview);
        searchView = (SearchView)view.findViewById(R.id.txtsearch);
        initList();

        return view;
    }

    public void initList()
    {
        String p = s.trim();
        items = p.split(",");
        listItems = new ArrayList<>(Arrays.asList(items));
        k=listItems;
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.txtitem,listItems);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                adapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.txtitem,filterl(text));
                k=filterl(text);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(HomeFragment.res==k.get(i))
                {
                    Toast.makeText(getActivity(),"Source and Destination cannot be same",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("Data", k.get(i));
                    getActivity().setResult(RESULT_OK, intent);
                    getActivity().finish();
                    Toast.makeText(getActivity(), k.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static ArrayList<String> filterl(String charText) {
        charText = charText.toLowerCase();
        ArrayList<String> al1 = new ArrayList<String>();
        if (charText.length() == 0) {
            al1.addAll(listItems);
        }
        else
        {
            for (Object wp : listItems) {
                String k=wp.toString();
                if (k.toLowerCase().contains(charText)) {
                    al1.add(wp.toString());
                }
            }
        }
        return al1;

    }



}
