package com.android.baker.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.baker.R;


public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    private TextView ingredientsListTextView;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ingredientsListTextView = itemView.findViewById(R.id.tv_ingredients_list);
    }

    public TextView getIngredientsListTextView() {
        return ingredientsListTextView;
    }

    public void setIngredientsListTextView(TextView ingredientsListTextView) {
        this.ingredientsListTextView = ingredientsListTextView;
    }
}
