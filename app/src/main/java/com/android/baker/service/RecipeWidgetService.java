package com.android.baker.service;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class RecipeWidgetService extends RemoteViewsService {

    public final static String TAG = RecipeWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d(TAG, "onGetViewFactory is called");
        return new IngredientsListFactory(getApplicationContext());
    }
}
