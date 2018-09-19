package com.android.baker.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Recipe implements Parcelable {

    private int id;
    private String name;
    private int servings;
    private String image;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<RecipeStep> recipeStepList;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";


    public Recipe() {
    }

    public Recipe(JSONObject jsonObj) throws JSONException {
        this.id = jsonObj.getInt(ID);
        this.name = jsonObj.getString(NAME);
        this.servings = jsonObj.getInt(SERVINGS);
        this.image = jsonObj.getString(IMAGE);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage() {
        this.image = image;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Ingredient> recipeIngredients) {
        this.ingredientList = recipeIngredients;
    }

    public ArrayList<RecipeStep> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(ArrayList<RecipeStep> recipeSteps) {
        this.recipeStepList = recipeSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(ingredientList);
        dest.writeTypedList(recipeStepList);
    }

    private Recipe(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        servings = parcel.readInt();
        image = parcel.readString();
        ingredientList = new ArrayList<>();
        parcel.readTypedList(ingredientList, Ingredient.CREATOR);
        recipeStepList = new ArrayList<>();
        parcel.readTypedList(recipeStepList, RecipeStep.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {

        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
