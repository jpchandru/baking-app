package com.android.baker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.baker.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule= new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testToolbarTextPositive(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Baker")).check(matches(withParent(withId(R.id.toolbar))));
    }

    @Test
    public void testToolbarTextNegative(){
        onView(withText("Bake")).check(doesNotExist());
    }

    @Test
    public void clickBrowniesItemPositive(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Brownies")).check(matches(withParent(withId(R.id.toolbar))));
    }

    @Test
    public void clickBrowniesItemNegative(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText("Brownie")).check(doesNotExist());
    }

}
