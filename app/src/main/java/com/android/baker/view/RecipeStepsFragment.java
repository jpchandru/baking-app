package com.android.baker.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.baker.R;
import com.android.baker.controller.RecipeStepsAdapter;
import com.android.baker.model.Recipe;
import com.android.baker.model.RecipeDetailsViewModel;
import com.android.baker.model.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.StepClickListener{

    private static final String LOG_TAG = RecipeStepsFragment.class.getSimpleName();
    private Recipe mRecipe;
    private String mRecipeName;
    private ArrayList<RecipeStep> mStepsList;
    private RecipeDetailsViewModel model;
    @BindView(R.id.rv_recipe_steps) RecyclerView mRecyclerView;

    public RecipeStepsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model= ViewModelProviders.of(getActivity()).get(RecipeDetailsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this,view);
        Bundle recipeBundle = this.getArguments();
        if (recipeBundle != null) {
            mRecipe = recipeBundle.getParcelable(MainActivity.RECIPE_KEY);
            mRecipeName= mRecipe.getName();
            mStepsList= mRecipe.getRecipeStepList();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new RecipeStepsAdapter(mRecipe, this));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStepClick(int clickedItemIndex) {
        int stepClickedIndex= clickedItemIndex -1;
        model.setRecipe(mRecipe);
        model.setmRecipeStep(stepClickedIndex);
        StepDetailsFragment detailsFragment= new StepDetailsFragment();
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        if(isTablet(getActivity())){
            fragmentTransaction.replace(R.id.fragment_container2, detailsFragment, null)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentTransaction.replace(R.id.recipe_container, detailsFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
