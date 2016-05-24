package com.sam_chordas.android.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aduran on 5/20/16.
 */
public class Quote implements Parcelable{

    public Quote() {

    }

    Quote(Parcel in) {
        this.symbol = in.readString();
        this.date = in.readString();
        this.close = in.readFloat();
        this.percentChange = in.readString();
        this.change = in.readString();
        this.isUp = in.readInt();
    }

    public String symbol;
    public String date;
    public float close;
    public String percentChange;
    public String change;
    public int isUp;


    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(date);
        dest.writeFloat(close);
        dest.writeString(percentChange);
        dest.writeString(change);
        dest.writeInt(isUp);
    }


}
