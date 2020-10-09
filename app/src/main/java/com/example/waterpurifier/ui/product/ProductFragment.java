package com.example.waterpurifier.ui.product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.waterpurifier.R;
import com.example.waterpurifier.databinding.FragmentProductBinding;
import com.example.waterpurifier.ui.home.Adapter_SPBanChay;
import com.example.waterpurifier.ui.home.Contact_SPBanChay;
import com.example.waterpurifier.ui.home.IonClickWaterPurifier;
import com.example.waterpurifier.ui.home.ItemSPBanChay;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment  extends Fragment {

    FragmentProductBinding binding;
    Adapter_SPBanChay adapter_spBanChay;

    List<Contact_SPBanChay> contact_spBanChays;
    String result1 = "";
    String urlAPI = "https://demo8117695.mockable.io/cacSanPhamKhac";
//    String urlAPI_HTLocNuoc = "https://demo8117695.mockable.io/list_HeThongLocnuoc";
//    String urlAPI_MayLocNuoc = "https://demo8117695.mockable.io/list_MayLocNuoc";
//    String urlAPI_LinhKien = "https://demo8117695.mockable.io/list_LinhKienMayLocNuoc";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);


        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result1 = response.toString();
                DoGetData_SpBanChay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.RCProductItem.setVisibility(View.INVISIBLE);
            }
        });
        requestQueue1.add(stringRequest1);


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        return binding.getRoot();
    }



    private void DoGetData_SpBanChay() {

        contact_spBanChays = new ArrayList<>();
        String name, image, old_price, content, status;
        int new_price;


        try {
            JSONArray jsonArray = new JSONArray(result1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                name = object.getString("publisher_id");
                new_price = object.getInt("id");
                image = object.getString("thumb");
                old_price = object.getString("old_price");
                content = object.getString("content");
                status = object.getString("status");
                contact_spBanChays.add(new Contact_SPBanChay(new_price, old_price, content, image, name, status));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RCProductItem.setLayoutManager(layoutManager);
        binding.RCProductItem.setAdapter(adapter_spBanChay);

        adapter_spBanChay.setIonClickWaterPurifier(new IonClickWaterPurifier() {
            @Override
            public void onClickItem(Contact_SPBanChay contact_spBanChay) {
                Fragment fragment = ItemSPBanChay.newInstance(contact_spBanChay, contact_spBanChays);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
    private void filter(String text)
    {
        ArrayList<Contact_SPBanChay> filter = new ArrayList<>();
        for(Contact_SPBanChay item : contact_spBanChays)
        {
            if(item.getName_product().toLowerCase().contains(text.toLowerCase()))
                filter.add(item);

        }
        adapter_spBanChay.filterList(filter);
    }
}
