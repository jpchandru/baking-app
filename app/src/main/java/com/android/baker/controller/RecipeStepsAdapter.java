package com.android.baker.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baker.R;
import com.android.baker.model.Ingredient;
import com.android.baker.model.Recipe;
import com.android.baker.model.RecipeStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = RecipeStepsAdapter.class.getSimpleName();
    private static final int TYPE_INGREDIENTS_LIST = 0;
    private static final int TYPE_RECIPE_STEP = 1;
    final private StepClickListener mOnClickListener;
    Recipe mRecipe;

    public interface StepClickListener {
        void onStepClick(int clickedItemIndex);
    }

    public RecipeStepsAdapter(Recipe recipe, StepClickListener listener) {
        this.mRecipe = recipe;
        mOnClickListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_INGREDIENTS_LIST) {
            //return new IngredientsViewHolder(null);
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_ingredients, parent, false);
            return new IngredientsViewHolder(view);
        } else if (viewType == TYPE_RECIPE_STEP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recipe_step, parent, false);
            return new RecipeStepsViewHolder(view);
        }
        throw new RuntimeException("No type of holder matching type " + String.valueOf(viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Depending on whether the holder is for ingredients or recipe steps,
        // bind the view accordingly
        if (holder instanceof IngredientsViewHolder) {
            ArrayList<Ingredient> ingredientList = mRecipe.getIngredientList();
            StringBuilder builder = new StringBuilder();
            for (Ingredient ingredient : ingredientList) {
                builder.append(ingredient.getIngredient() + "\n");
            }
            // Cast holder to IngredientsViewHolder to set the text in tv_ingredients_list
            ((IngredientsViewHolder) holder).getIngredientsListTextView().setText(builder.toString());
        } else if (holder instanceof RecipeStepsViewHolder) {
            ArrayList<RecipeStep> stepsList = mRecipe.getRecipeStepList();
            if (position < stepsList.size() + 1) {
                RecipeStep step = stepsList.get(position - 1);
                String shortDescription = step.getShortDescription();
                String recipeStepImageString = step.getThumbnailUrl();

                if (!recipeStepImageString.equals("")) {
                    Picasso.get()
                            .load(recipeStepImageString)
                            .into(((RecipeStepsViewHolder) holder).recipeStepImageView);
                }
                ((RecipeStepsViewHolder) holder).recipeStepTextView.setText(shortDescription);

            }

        }

    }


    /**
     * getItemViewType is used to map the view type to the position in the adapter
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_INGREDIENTS_LIST;
            default:
                return TYPE_RECIPE_STEP;
        }

    }

    @Override
    public int getItemCount() {
        return mRecipe.getRecipeStepList().size() + 1;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.tv_ingredients_list);
        }
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeStepTextView;
        ImageView recipeStepImageView;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            recipeStepTextView = itemView.findViewById(R.id.tv_recipe_step);
            recipeStepImageView = itemView.findViewById(R.id.iv_recipe_step_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            // When clicking, passing the clicked item's position
            mOnClickListener.onStepClick(clickedPosition);
        }
    }
}
