package com.example.waterpurifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.waterpurifier.databinding.FragmentRegistrationBinding;
import com.example.waterpurifier.ui.SQLite.SQLite_Account;

public class Fragment_Registraion extends Fragment {
    FragmentRegistrationBinding binding;
    Toolbar toolbar;
    LinearLayout linearLayout;
    public static Fragment_Registraion newInstance() {

        Bundle args = new Bundle();

        Fragment_Registraion fragment = new Fragment_Registraion();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
   SetToolBar();
        binding.singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.userName.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String xacnhan = binding.Confirm.getText().toString().trim();
                String phonenumber = binding.PhoneNumber.getText().toString().trim();
                String address = binding.Address.getText().toString().trim();
                String name = binding.Name.getText().toString().trim();

                if(username.length() == 0)
                {
                    Toast.makeText(getContext(),"Tài Khoản Không Được Để Trống.",Toast.LENGTH_LONG).show();
                    return;
                }
               else if(password.length() <8)
                {
                    Toast.makeText(getContext(),"Mật Khẩu Phải Lớn Hơn 8 Ký Tự.",Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.equals(xacnhan))
                {
                    SQLite_Account db = new SQLite_Account(getContext());
                    long value = db.addUser(username,password,phonenumber,address,name);

                    if (value >0)
                    {
//                        Intent intent = new Intent(getContext(),LoginActivity.class);
//                        startActivity(intent);
                        Fragment listContactFragment = LoginFragemnt.newInstance();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
                        Toast.makeText(getContext(),"Đăng Ký Thành Công.",Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

        binding.loginRegistraion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listContactFragment = LoginFragemnt.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
            }
        });
        return binding.getRoot();
    }
    public void SetToolBar(){
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        linearLayout = getActivity().findViewById(R.id.layout_top);
        linearLayout.setVisibility(View.GONE);
    }
}
