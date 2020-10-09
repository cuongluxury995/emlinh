package com.example.waterpurifier.ui.home;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.waterpurifier.R;
import com.example.waterpurifier.databinding.FragmentItemSpbanchayBinding;
import com.example.waterpurifier.ui.SQLite.SQL_Helper;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemSPBanChay extends Fragment {
    FragmentItemSpbanchayBinding binding;
    Contact_SPBanChay contact_spBanChay;
    List<Contact_SPBanChay> contact_spBanChays, contact_spBanChayList;
    Fragment is = this;

    public static ItemSPBanChay newInstance(Contact_SPBanChay contact_spBanChay, List<Contact_SPBanChay> contact_spBanChays) {

        Bundle args = new Bundle();
        args.putSerializable("contact_spBanChay", contact_spBanChay);
        args.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) contact_spBanChays);
        ItemSPBanChay fragment = new ItemSPBanChay();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_spbanchay, container, false);

        contact_spBanChay = (Contact_SPBanChay) getArguments().getSerializable("contact_spBanChay");
        contact_spBanChays = (List<Contact_SPBanChay>) getArguments().getSerializable("list");
        Picasso.with(getContext()).load(contact_spBanChay.getImage()).into(binding.igItemSpbanchay);
        binding.tvItemTitle.setText(contact_spBanChay.getName_product());
        binding.tvItemOldPrice.setText(contact_spBanChay.getOld_price());

        Locale local = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(local);
        String money = numberFormat.format(contact_spBanChay.getNew_price());
        binding.tvItemNewPrice.setText(money + "VNĐ");
        binding.tvItemStatus.setText(contact_spBanChay.getStatus());
        binding.tvItemContent.setText(contact_spBanChay.getContent());
        binding.tvItemOldPrice.setPaintFlags(binding.tvItemOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        binding.igBackListHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                manager.getBackStackEntryCount();
                transaction.remove(is);
                transaction.commit();
            }
        });



        binding.addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQL_Helper sql_helper;
                sql_helper = new SQL_Helper(getContext());
                sql_helper.insertCart(new Contact_SPBanChay(contact_spBanChay.getNew_price(), contact_spBanChay.getOld_price(), contact_spBanChay.getContent(), contact_spBanChay.getImage(), contact_spBanChay.getName_product(), contact_spBanChay.getStatus()));
                Toast.makeText(getContext(), "thêm vào giỏ hàng thành công", Toast.LENGTH_LONG).show();
            }
        });
        contact_spBanChayList = new ArrayList<>();

        for (Contact_SPBanChay sp : contact_spBanChays) {
            if (sp.getName_product().equals(contact_spBanChay.getName_product())) ;
            contact_spBanChayList.add(sp);
        }

        Adapter_SPBanChay adapter_spBanChay2 = new Adapter_SPBanChay(contact_spBanChayList, getContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.RCmoreSPChay.setAdapter(adapter_spBanChay2);
        binding.RCmoreSPChay.setLayoutManager(layoutManager2);


        adapter_spBanChay2.setIonClickWaterPurifier(new IonClickWaterPurifier() {
            @Override
            public void onClickItem(Contact_SPBanChay contact_spBanChay) {
                Fragment fragment = ItemSPBanChay.newInstance(contact_spBanChay,contact_spBanChays);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return binding.getRoot();
    }

}

