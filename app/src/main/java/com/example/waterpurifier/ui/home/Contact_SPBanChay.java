package com.example.waterpurifier.ui.home;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Contact_SPBanChay implements Parcelable, Serializable{
    private int new_price;
    private String old_price;
    private String content;
    private String image;
    private String name_product;
    private String status;

    public Contact_SPBanChay(int new_price, String old_price, String content, String image, String name_product, String status) {
        this.new_price = new_price;
        this.old_price = old_price;
        this.content = content;
        this.image = image;
        this.name_product = name_product;
        this.status = status;
    }


    public int getNew_price() {
        return new_price;
    }

    public void setNew_price(int new_price) {
        this.new_price = new_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public static Creator<Contact_SPBanChay> getCREATOR() {
        return CREATOR;
    }

    public static final Parcelable.Creator<Contact_SPBanChay> CREATOR = new Parcelable.Creator<Contact_SPBanChay>() {
        @Override
        public Contact_SPBanChay createFromParcel(Parcel in) {
            return new Contact_SPBanChay(in);
        }

        @Override
        public Contact_SPBanChay[] newArray(int size) {
            return new Contact_SPBanChay[size];
        }
    };

    protected Contact_SPBanChay(Parcel in) {
        old_price = in.readString();
        new_price = in.readInt();
        image = in.readString();
        name_product = in.readString();
        content = in.readString();
        status = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name_product);
        dest.writeString(image);
        dest.writeString(old_price);
        dest.writeInt(new_price);
        dest.writeString(content);
        dest.writeString(status);
    }
}
