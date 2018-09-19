package com.android.baker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.baker.model.Recipe;
import com.google.gson.Gson;

public class Preferences {

    public static final String TAG = Preferences.class.getSimpleName();
    public static final String PREFS_NAME = "preferences";
    public static final String RECIPE = "recipe";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        // Use Gson to convert Recipe to json and store it as a SharedPreference
        Gson gson = new Gson();
        String recipeJson = gson.toJson(recipe);
        sharedPreferences.putString(RECIPE, recipeJson);
        Log.d(TAG, recipe.getName() + " was saved in SharedPreferences");
        sharedPreferences.apply();

    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Use Gson to convert back to Recipe
        Gson gson = new Gson();
        String recipeJson = sharedPreferences.getString(RECIPE, null);
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
        return recipe;
    }


}
