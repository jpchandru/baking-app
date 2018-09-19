package com.android.baker.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Ingredient implements Parcelable {

    private long quantity;
    private String measure;
    private String ingredient;

    public Ingredient(JSONObject jsonObject) {
        this.quantity = jsonObject.optInt("quantity");
        this.measure = jsonObject.optString("measure");
        this.ingredient = jsonObject.optString("ingredient");
    }

    protected Ingredient(Parcel parcel) {
        quantity = parcel.readLong();
        measure = parcel.readString();
        ingredient = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public Ingredient() {
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
