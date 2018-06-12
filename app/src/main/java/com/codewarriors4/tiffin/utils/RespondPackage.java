package com.codewarriors4.tiffin.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class RespondPackage implements Parcelable {
    private Map<String, String> params = new HashMap<>();
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public RespondPackage() {
    }

    protected RespondPackage(Parcel in) {
        int paramsSize = in.readInt();
        this.params = new HashMap<String, String>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
    }

    public static final Parcelable.Creator<RespondPackage> CREATOR = new Parcelable.Creator<RespondPackage>() {
        @Override
        public RespondPackage createFromParcel(Parcel source) {
            return new RespondPackage(source);
        }

        @Override
        public RespondPackage[] newArray(int size) {
            return new RespondPackage[size];
        }
    };

    public Map<String, String> getParams()
    {
        return params;
    }

    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }
    public void setParam(String key, String value)
    {
        params.put(key, value);
    }
}
