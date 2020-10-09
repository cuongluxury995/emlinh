package com.example.waterpurifier.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.waterpurifier.R;
import com.example.waterpurifier.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Adapter_SPBanChay adapter_spBanChay;

    List<Contact_SPBanChay> contact_spBanChays;

    String result1 = "";
    String urlAPI = "https://demo8117695.mockable.io/list_SanPhamBanChayNhat";
    String urlAPI_MayDG = "https://demo8117695.mockable.io/list_MayDienGiai";
    String urlAPI_HTLocNuoc = "https://demo8117695.mockable.io/list_HeThongLocnuoc";
    String urlAPI_MayLocNuoc = "https://demo8117695.mockable.io/list_MayLocNuoc";
    String urlAPI_LinhKien = "https://demo8117695.mockable.io/list_LinhKienMayLocNuoc";

    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        
        setlayout();
         
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI_MayDG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result1 = response.toString();
                DoGetData_MayDG();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.RcSPDienGiai.setVisibility(View.INVISIBLE);
            }
        });

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
                binding.RcSPBanChay.setVisibility(View.INVISIBLE);
            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, urlAPI_HTLocNuoc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result1 = response.toString();
                DoGetData_HeThongLocNuoc();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.RcSPLocNuoc.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, urlAPI_MayLocNuoc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result1 = response.toString();
                DoGetData_MayLocNuoc();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.RcMayLocNuoc.setVisibility(View.INVISIBLE);
            }
        });

        RequestQueue requestQueue4 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, urlAPI_LinhKien, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result1 = response.toString();
                DoGetData_LinhKien();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.RcLinhKien.setVisibility(View.INVISIBLE);
            }
        });
        requestQueue2.add(stringRequest2);
        requestQueue3.add(stringRequest3);
        requestQueue4.add(stringRequest4);
        requestQueue1.add(stringRequest1);
        requestQueue.add(stringRequest);
      
        return binding.getRoot();
    }

    private void setlayout() {
        ArrayList<String> slide = new ArrayList<>();
        slide.add("https://i.imgur.com/2pKMat0.jpg");
        slide.add("https://i.imgur.com/NZMXeby.jpg");
        slide.add("https://i.imgur.com/2pKMat0.jpg");
        slide.add("https://i.imgur.com/NZMXeby.jpg");
        slide.add("https://i.imgur.com/2pKMat0.jpg");

        for (int i = 0; i < slide.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            Picasso.with(getContext()).load(slide.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.Viewflipper.addView(imageView);
        }
        //cho tu chay trong 5s
        binding.Viewflipper.setFlipInterval(4000);
        binding.Viewflipper.setAutoStart(true);
        //goi Animation de anh chay tu tu
         @SuppressLint("ResourceType") Animation animation_slide_in = AnimationUtils.loadAnimation(getContext(), R.transition.slide_in_right);
         @SuppressLint("ResourceType") Animation animation_slide_out = AnimationUtils.loadAnimation(getContext(), R.transition.slide_out_right);
        binding.Viewflipper.setInAnimation(animation_slide_in);
        binding.Viewflipper.setOutAnimation(animation_slide_out);
    }

    private void DoGetData_MayDG() {


        contact_spBanChays = new ArrayList<>();
        String name, image,old_price,content,status;
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
                contact_spBanChays.add(new Contact_SPBanChay(new_price,old_price,content,image,name,status));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RcSPDienGiai.setLayoutManager(layoutManager);
        binding.RcSPDienGiai.setAdapter(adapter_spBanChay);

        adapter_spBanChay.setIonClickWaterPurifier(new IonClickWaterPurifier() {
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




    }

    private void DoGetData_SpBanChay() {

        contact_spBanChays = new ArrayList<>();
        String name, image,old_price,content,status;
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
                contact_spBanChays.add(new Contact_SPBanChay(new_price,old_price,content,image,name,status));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RcSPBanChay.setLayoutManager(layoutManager);
        binding.RcSPBanChay.setAdapter(adapter_spBanChay);

        adapter_spBanChay.setIonClickWaterPurifier(new IonClickWaterPurifier() {
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



    }
    private void DoGetData_LinhKien() {


        contact_spBanChays = new ArrayList<>();
        String name, image,old_price,content,status;
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
                contact_spBanChays.add(new Contact_SPBanChay(new_price,old_price,content,image,name,status));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RcLinhKien.setLayoutManager(layoutManager);
        binding.RcLinhKien.setAdapter(adapter_spBanChay);

        adapter_spBanChay.setIonClickWaterPurifier(new IonClickWaterPurifier() {
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



    }
    private void DoGetData_HeThongLocNuoc() {


        contact_spBanChays = new ArrayList<>();
        String name, image,old_price,content,status;
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
                contact_spBanChays.add(new Contact_SPBanChay(new_price,old_price,content,image,name,status));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RcSPLocNuoc.setLayoutManager(layoutManager);
        binding.RcSPLocNuoc.setAdapter(adapter_spBanChay);

        adapter_spBanChay.setIonClickWaterPurifier(new IonClickWaterPurifier() {
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



    }
    private void DoGetData_MayLocNuoc() {

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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        adapter_spBanChay = new Adapter_SPBanChay(contact_spBanChays, getContext());
        binding.RcMayLocNuoc.setLayoutManager(layoutManager);
        binding.RcMayLocNuoc.setAdapter(adapter_spBanChay);

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

    }

