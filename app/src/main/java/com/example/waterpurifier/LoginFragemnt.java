package com.example.waterpurifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.waterpurifier.databinding.FragmentLoginBinding;
import com.example.waterpurifier.ui.SQLite.SQLite_Account;
import com.example.waterpurifier.ui.cart.CartFragment;

public class LoginFragemnt extends Fragment {
    FragmentLoginBinding binding;
    Toolbar toolbar;
    LinearLayout linearLayout;

    public static LoginFragemnt newInstance() {

        Bundle args = new Bundle();

        LoginFragemnt fragment = new LoginFragemnt();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        SetToolBar();
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.userName.getText().toString();
                String password = binding.password.getText().toString();
                SQLite_Account sqLite_account = new SQLite_Account(getContext());
                if (userName.length() == 0 || password.length() == 0) {
                    Toast.makeText(getContext(), "Bạn Phải Nhập Đầy Đủ Thông Tin", Toast.LENGTH_LONG).show();
                    return;
                } else if (sqLite_account.chekUser(userName, password)) {
                   Toast.makeText(getContext(), "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                    Fragment listContactFragment = CartFragment.newInstance(true);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
                    GetToolBar();
//                    Intent intent = new Intent(getContext(),MainActivity.class);
//                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Tài Khoản Hoặc Mật Khẩu Không Chính Xác", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

        binding.singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getContext(),activity_regisstration.class);
//                startActivity(intent);
                Fragment listContactFragment = Fragment_Registraion.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
                SetToolBar();
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
    public void GetToolBar(){
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        linearLayout = getActivity().findViewById(R.id.layout_top);
        linearLayout.setVisibility(View.VISIBLE);
    }

}
