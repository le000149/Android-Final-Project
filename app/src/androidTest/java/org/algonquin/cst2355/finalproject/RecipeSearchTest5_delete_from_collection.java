package org.algonquin.cst2355.finalproject;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeSearchTest5_delete_from_collection {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void recipeSearchTest5_delete_from_collection() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.recipe), withContentDescription("Recipe Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                1),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("beef"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.commonRecycleView),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.details_btn), withText("Collect"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.wd),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.common_recycleView),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.details_btn2), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.buttonPanel),
                                        0),
                                2)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.details_btn2), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.common_recycleView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView3.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
