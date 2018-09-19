package com.android.baker.view;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.baker.R;
import com.android.baker.controller.RecipeAdapter;
import com.android.baker.model.Ingredient;
import com.android.baker.model.Recipe;
import com.android.baker.model.RecipeDetailsViewModel;
import com.android.baker.model.RecipeStep;
import com.android.baker.utils.Preferences;
import com.android.baker.service.RecipeWidgetProvider;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener{

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String RECIPE_KEY= "recipe-key";
    public static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeArrayList;
    private RequestQueue requestQueue;
    private RecipeDetailsViewModel model;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mRecipeArrayList = new ArrayList<>();
        model= ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);

        createRecipes();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeArrayList, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    private void createRecipes() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++)
                            try {
                                JSONObject jsonRecipe = response.getJSONObject(i);
                                JSONArray ingredientsJsonArray= jsonRecipe.getJSONArray(INGREDIENTS);
                                JSONArray stepsJsonArray= jsonRecipe.getJSONArray(STEPS);
                                ArrayList<Ingredient> ingredientsArrayList= new ArrayList<>();
                                for(int j=0; j<ingredientsJsonArray.length(); j++){
                                    JSONObject jsonIngredient= ingredientsJsonArray.getJSONObject(j);
                                    Ingredient ingredient= new Ingredient(jsonIngredient);
                                    ingredientsArrayList.add(ingredient);
                                }
                                ArrayList<RecipeStep> stepsArrayList= new ArrayList<>();
                                for(int k=0; k<stepsJsonArray.length(); k++){
                                    JSONObject jsonStep= stepsJsonArray.getJSONObject(k);
                                    RecipeStep recipeStep= new RecipeStep(jsonStep);
                                    stepsArrayList.add(recipeStep);
                                }
                                Recipe recipe = new Recipe(jsonRecipe);
                                recipe.setIngredientList(ingredientsArrayList);
                                recipe.setRecipeStepList(stepsArrayList);
                                mRecipeArrayList.add(recipe);
                                mRecipeAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(LOG_TAG, "Error: " + e.getMessage());
                            }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(LOG_TAG, "Error: " + error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    public void onRecipeClick(int clickedRecipePosition) {
        Recipe recipe= mRecipeArrayList.get(clickedRecipePosition);
        Preferences.saveRecipe(this, recipe);

        if(isTablet(this)){
            Log.d(LOG_TAG, "Tablet Mode ON");
        }
        // Broadcast the changes to the WidgetProvider
        Intent notifyWidgetIntent= new Intent(this, RecipeWidgetProvider.class);
        notifyWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids=AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), RecipeWidgetProvider.class));
        notifyWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(notifyWidgetIntent);

        Intent recipeIntent= new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RECIPE_KEY, recipe);
        startActivity(recipeIntent);

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
