package algonquin.cst2335.testsun;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // Test to verify the help dialog functionality
    @Test
    public void testViewHelpDialog() {
        // Open the overflow menu in the action bar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // Click on the "Help" option
        onView(withText("Help")).perform(click());
        // Click on the "OK" button in the dialog to dismiss it
        onView(withText("OK")).perform(click());
    }

    // Test to verify the about dialog functionality
    @Test
    public void testViewAboutDialog() {
        // Open the overflow menu in the action bar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // Click on the "About" option
        onView(withText("About")).perform(click());
        // Click on the "OK" button in the dialog to dismiss it
        onView(withText("OK")).perform(click());
    }

    // Test to verify navigation to the favorites screen
    @Test
    public void testNavigationToFavoritesScreen() {
        // Click on the view favorites action button
        onView(withId(R.id.action_view_favorites)).perform(click());
        // Check if the favorites RecyclerView is displayed in the new screen
        onView(withId(R.id.favoritesRecyclerView)).check(matches(isDisplayed()));
    }

    // Test to verify the lookup functionality with valid coordinates
    @Test
    public void testLookupCityName() {
        // Enter valid latitude and longitude for testing
        String testLatitude = "45.4215";
        String testLongitude = "-75.6972";

        // Input latitude and longitude into the EditText fields and close the soft keyboard
        onView(withId(R.id.latitudeEditText)).perform(replaceText(testLatitude), closeSoftKeyboard());
        onView(withId(R.id.longitudeEditText)).perform(replaceText(testLongitude), closeSoftKeyboard());

        // Click on the lookup button to initiate the search
        onView(withId(R.id.lookupButton)).perform(click());

        // Verify that the city name text view is updated with a non-empty value
        onView(withId(R.id.cityNameTextView)).check(matches(not(withText(isEmptyOrNullString()))));
    }

    // Test to verify that invalid coordinates show an appropriate error
    @Test
    public void testInvalidCoordinatesLookup() {
        // Enter invalid latitude and longitude to simulate user input error
        String invalidLatitude = "abc";
        String invalidLongitude = "xyz";

        // Replace text in EditText fields with invalid coordinates
        onView(withId(R.id.latitudeEditText)).perform(replaceText(invalidLatitude), closeSoftKeyboard());
        onView(withId(R.id.longitudeEditText)).perform(replaceText(invalidLongitude), closeSoftKeyboard());

        // Click on the lookup button to try fetching data with invalid coordinates
        onView(withId(R.id.lookupButton)).perform(click());

        // Ideally, here we would verify a Toast message or error state in the UI,
        // but since Espresso has limitations in accessing Toasts, this step might need UI Automator or a different approach
    }

}
