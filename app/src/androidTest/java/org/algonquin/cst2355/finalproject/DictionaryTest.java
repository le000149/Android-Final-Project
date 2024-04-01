package org.algonquin.cst2355.finalproject;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class DictionaryTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void dict1ActivityLaunchTest() {
        onView(withId(R.id.dictionary)).perform(click());
        ViewInteraction textView = onView(withText("Dictionary"));
        textView.check(matches(withText("Dictionary")));
    }

    @Test
    public void dict2SaveDefinition() throws InterruptedException {
        ViewInteraction actionMenuItemView = onView(withId(R.id.dictionary));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.search_edit_text));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.search_button));
        materialButton.perform(click());

        Thread.sleep(1000);

        ViewInteraction actionMenuItemView2 = onView(withId(R.id.save_or_delete_definition));
        actionMenuItemView2.perform(click());

        pressBack();

        ViewInteraction textView = onView(
                allOf(withId(R.id.word_text_view), withText("hello"),
                        withParent(withParent(withId(R.id.saved_definition_recycler_view))),
                        isDisplayed()));
        textView.check(matches(withText("hello")));
    }

    @Test
    public void dict3SearchFromDB() throws InterruptedException {
        onView(withId(R.id.dictionary)).perform(click());

        ViewInteraction recyclerView = onView(withId(R.id.saved_definition_recycler_view));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);

        ViewInteraction textView = onView(withText("\"Hello!\" or an equivalent greeting."));
        textView.check(matches(withText("\"Hello!\" or an equivalent greeting.")));
    }

    @Test
    public void dict4DeleteDefinition() throws InterruptedException {
        ViewInteraction actionMenuItemView = onView(withId(R.id.dictionary));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.search_edit_text));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.search_button));
        materialButton.perform(click());

        Thread.sleep(1000);

        ViewInteraction actionMenuItemView2 = onView(withId(R.id.save_or_delete_definition));
        actionMenuItemView2.perform(click());

        pressBack();

        ViewInteraction recyclerView = onView(withId(R.id.saved_definition_recycler_view));
        recyclerView.check(matches(hasChildCount(0)));
    }

    @Test
    public void dict5LongPressDelete() throws InterruptedException {
        ViewInteraction actionMenuItemView = onView(withId(R.id.dictionary));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.search_edit_text));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.search_button));
        materialButton.perform(click());

        Thread.sleep(1000);

        ViewInteraction actionMenuItemView2 = onView(withId(R.id.save_or_delete_definition));
        actionMenuItemView2.perform(click());

        pressBack();

        ViewInteraction recyclerView = onView(withId(R.id.saved_definition_recycler_view));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        ViewInteraction materialButton2 = onView(withId(android.R.id.button1));
        materialButton2.perform(scrollTo(), click());

        recyclerView.check(matches(hasChildCount(0)));
    }

    @Test
    public void dict6ResultActivityTest() throws InterruptedException {
        ViewInteraction actionMenuItemView = onView(withId(R.id.dictionary));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.search_edit_text));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.search_button));
        materialButton.perform(click());

        Thread.sleep(1000);

        ViewInteraction textView = onView(withText("\"Hello!\" or an equivalent greeting."));
        textView.check(matches(withText("\"Hello!\" or an equivalent greeting.")));
    }

}
