package com.example.hariharansivakumar.tike;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    TextView email,phone,name;
    Button logout;
    public static ProfileFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ProfileFragment firstFragment = new ProfileFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
                    getActivity().finish();
                    SharedPrefManager.getInstance(getActivity()).logout();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email.setText(user.getEmail());
        name.setText(user.getUsername());
        phone.setText("+91-"+Long.toString(user.getMobile()));

        return view;

    }

}
