package com.example.waterpurifier.ui.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterpurifier.LoginActivity;
import com.example.waterpurifier.LoginFragemnt;
import com.example.waterpurifier.R;
import com.example.waterpurifier.databinding.FragmentCarBinding;
import com.example.waterpurifier.ui.home.Adapter_list_car;
import com.example.waterpurifier.ui.home.Contact_SPBanChay;
import com.example.waterpurifier.ui.home.HomeFragment;
import com.example.waterpurifier.ui.home.IconLongClick;
import com.example.waterpurifier.ui.SQLite.SQL_Helper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {
    private static final String TAG = "CarFragment";
    LoginFragemnt loginFragemnt;
    FragmentCarBinding binding;
    SQL_Helper sql_helper;
    List<Contact_SPBanChay> listcar;
    Fragment is = this;
    static int dem = 1;
     static boolean  check;
    public static CartFragment newInstance(boolean checked) {
        check = checked;
        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car, container, false);

        listcar = new ArrayList<>();
        sql_helper = new SQL_Helper(getContext());
        listcar = sql_helper.getallProduct();
        final Adapter_list_car adapter_custom_car = new Adapter_list_car(listcar, getContext());
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        binding.RCListSpCar.setAdapter(adapter_custom_car);
        binding.RCListSpCar.setLayoutManager(layoutManager);

        if (listcar.size() == 0) {
            binding.tvThongBao.getResources().getColor(R.color.colorAccent);
            binding.tvThongBao.setText("Không Có Sản Phẩm Nào Trong Giỏ Hàng!!!");
        }
        int sum = 0;

        for (Contact_SPBanChay sp : listcar) {
            if (sp.getNew_price() > 0) {
                sum += sp.getNew_price();
            }
        }
        final Locale local = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(local);
        String money = numberFormat.format(sum);
        binding.sumPriceCar.setText(String.valueOf(money) + (" VNĐ"));


        adapter_custom_car.seticonLongClick(new IconLongClick() {
            @Override
            public void onClickButton(final Contact_SPBanChay contact_spBanChay) {

                if (listcar.size() == 0) {
                    binding.tvThongBao.getResources().getColor(R.color.colorAccent);
                    binding.tvThongBao.setText("Không Có Sản Phẩm Nào Trong Giỏ Hàng!!!");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //Cài đặt các thuộc tính
                builder.setTitle("Xác nhận !");
                builder.setMessage("Bạn Thực Sự Muốn Xóa Sản Phẩm Này ?");
                builder.setIcon(R.drawable.delete_product);
                // Cài đặt button Cancel- Hiển thị Toast
                builder.setPositiveButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Hủy Bỏ", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                // Cài đặt button Yes Dismiss ẩn Dialog
                builder.setNegativeButton("Đồng Ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        boolean check = sql_helper.deleteItemInCart(contact_spBanChay.getName_product());
                        listcar = sql_helper.getallProduct();
                        Adapter_list_car adapter_custom_car = new Adapter_list_car(listcar, getContext());
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
                        binding.RCListSpCar.setAdapter(adapter_custom_car);
                        binding.RCListSpCar.setLayoutManager(layoutManager);
                        set_Car();

//                        adapter_custom_car.seticonLongClick(new IconLongClick() {
//                            @Override
//                            public void onClickButton(final Contact_SPBanChay contact_spBanChay) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                //Cài đặt các thuộc tính
//                                builder.setTitle("Xác nhận !");
//                                builder.setMessage("Bạn Thực Sự Muốn Xóa Sản Phẩm Này ?");
//                                builder.setIcon(R.drawable.icon_phone);
//                                // Cài đặt button Cancel- Hiển thị Toast
//                                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                                        dialog.cancel();
                               //    }
                              //  });
                                // Cài đặt button Yes Dismiss ẩn Dialog
//                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        boolean check = sql_helper.deleteItemInCart(contact_spBanChay.getName_product());
//                                        listcar = sql_helper.getallProduct();
//                                        Adapter_list_car adapter_custom_car = new Adapter_list_car(listcar, getContext());
//                                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
//                                        binding.RCListSpCar.setAdapter(adapter_custom_car);
//                                        binding.RCListSpCar.setLayoutManager(layoutManager);
//                                        set_Car();
//
//                                        adapter_custom_car.seticonLongClick(new IconLongClick() {
//                                            @Override
//                                            public void onClickButton(final Contact_SPBanChay contact_spBanChay) {
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                //Cài đặt các thuộc tính
//                                                builder.setTitle("Xác nhận !");
//                                                builder.setMessage("Bạn Thực Sự Muốn Xóa Sản Phẩm Này ?");
//                                                builder.setIcon(R.drawable.icon_phone);
//                                                // Cài đặt button Cancel- Hiển thị Toast
//                                                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                                                        dialog.cancel();
//                                                    }
//                                                });
//                                                // Cài đặt button Yes Dismiss ẩn Dialog
//                                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        boolean check = sql_helper.deleteItemInCart(contact_spBanChay.getName_product());
//                                                        listcar = sql_helper.getallProduct();
//                                                        Adapter_list_car adapter_custom_car = new Adapter_list_car(listcar, getContext());
//                                                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
//                                                        binding.RCListSpCar.setAdapter(adapter_custom_car);
//                                                        binding.RCListSpCar.setLayoutManager(layoutManager);
//                                                        set_Car();
//
//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                        //Cài đặt các thuộc tính
//                                                        builder.setTitle("Xác nhận !");
//                                                        builder.setMessage("Bạn Thực Sự Muốn Xóa Sản Phẩm Này ?");
//                                                        builder.setIcon(R.drawable.icon_phone);
//                                                        // Cài đặt button Cancel- Hiển thị Toast
//                                                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                        // Cài đặt button Yes Dismiss ẩn Dialog
//                                                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                boolean check = sql_helper.deleteItemInCart(contact_spBanChay.getName_product());
//                                                                listcar = sql_helper.getallProduct();
//                                                                Adapter_list_car adapter_custom_car = new Adapter_list_car(listcar, getContext());
//                                                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
//                                                                binding.RCListSpCar.setAdapter(adapter_custom_car);
//                                                                binding.RCListSpCar.setLayoutManager(layoutManager);
//                                                                set_Car();
//
//                                                            }
//                                                        })
//                                                                .create();
//
//                                                        builder.show();
//                                                    }
//                                                })
//                                                        .create();
//
//                                                builder.show();
//                                            }
//                                        });
//                                    }
//                                })
//                                        .create();
//
//                                builder.show();
                            }
                 //       });
               //     }
                })
                        .create();

                builder.show();
            }


        });


        binding.btRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listcar.size() == 0) {
                    Toast.makeText(getContext(), "Giỏ Hàng Không Có Sản Phẩm Nào", Toast.LENGTH_LONG).show();
                    return;
                }
                if (listcar.size() > 0) {
                   // if (dem != 1) {
                        if(check ==true) {
                            loginFragemnt = new LoginFragemnt();

                            final String[] tunes = {"Thanh Toán Online", "Thanh Toán Khi Nhận Hàng"};
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                    .setTitle("Các Cách Thanh Toán Khi Mua Hàng")
                                    .setIcon(R.drawable.icon_phone)
//                        .setMessage("SetMessenger")
                                    .setSingleChoiceItems(tunes, 1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    })
                                    .setPositiveButton("Xác Nhận", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            i=0;
                                            if (tunes[i].equals("Thanh Toán Online")) {
                                                binding.tvThongBao.getResources().getColor(R.color.colorAccent);
                                                binding.tvThongBao.setText("Không Có Sản Phẩm Nào Trong Giỏ Hàng!!!");
                                                binding.sumPriceCar.setText("0 VNĐ");
                                                boolean listcar = sql_helper.deleteAllProtect();
                                                Toast.makeText(getContext(), "Thanh Toán Thành Công", Toast.LENGTH_LONG).show();
                                                binding.RCListSpCar.setAdapter(null);

                                            } else {
                                                binding.tvThongBao.getResources().getColor(R.color.colorAccent);
                                                binding.tvThongBao.setText("Không Có Sản Phẩm Nào Trong Giỏ Hàng!!!");
                                                binding.sumPriceCar.setText("0 VNĐ");
                                                boolean listcar = sql_helper.deleteAllProtect();
                                                binding.RCListSpCar.setAdapter(null);
                                                Toast.makeText(getContext(), "Thanh Toán Khi Nhận Hàng", Toast.LENGTH_LONG).show();
                                                binding.RCListSpCar.setLayoutManager(null);
                                            }
                                            if (listcar.size() == 0) {
                                                Toast.makeText(getContext(), "Giỏ Hàng Không Có Sản Phẩm Nào", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                    })
                                    .setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //Cài đặt các thuộc tính
                        builder.setTitle("Xác nhận !");
                        builder.setMessage("Muốn Thanh Toán Sản Phẩm Khách Hàng Vui Lòng Đăng Nhập Vào Ứng Dụng ");
                        builder.setIcon(R.drawable.pay);
                        // Cài đặt button Cancel- Hiển thị Toast
                        builder.setPositiveButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Hủy Bỏ", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        // Cài đặt button Yes Dismiss ẩn Dialog
                        builder.setNegativeButton("Đồng Ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dem--;
//                                Intent intent = new Intent(getContext(), LoginActivity.class);
//                                startActivity(intent);
//
                                Fragment listContactFragment = LoginFragemnt.newInstance();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
                            }
                        })
                                .create();

                        builder.show();
                    }
                }
            }
        });
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment listContactFragment = HomeFragment.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, listContactFragment).commit();
                fragmentTransaction.addToBackStack(null);

            }
        });
        return binding.getRoot();
    }
    public int sum_cart()
    {
        int sum = 0;

        for (Contact_SPBanChay sp : listcar) {
            if (sp.getNew_price() > 0) {
                sum += sp.getNew_price();
            }
        }
        return sum;
    }
    public void set_Car()
    {

        Locale local = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(local);
        String money = numberFormat.format(sum_cart());
        binding.sumPriceCar.setText(String.valueOf(money) + (" VNĐ"));
    }

}


