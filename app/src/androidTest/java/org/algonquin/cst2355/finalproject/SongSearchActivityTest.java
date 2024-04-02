package org.algonquin.cst2355.finalproject;


import androidx.test.core.app.ActivityScenario;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.algonquin.cst2355.finalproject.songsearch.SongSearchActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * This class contains Espresso UI tests for the SongSearchActivity.
 * Author: Hoang Anh Nguyen - 041099695
 * Lab Section - 013
 */
@RunWith(AndroidJUnit4.class)
public class SongSearchActivityTest {
    private ActivityScenario<SongSearchActivity> activityScenario;

    /**
     * Sets up the test environment before each test method.
     */
    @Before
    public void setUp() {
        activityScenario = ActivityScenario.launch(SongSearchActivity.class);
    }
    /**
     * Tears down the test environment after each test method.
     */
    @After
    public void tearDown() {
        activityScenario.close();
    }
    /**
     * Tests the functionality of EditText for searching an artist.
     */
    @Test
    public void testEditTextSearch() {
        onView(ViewMatchers.withId(R.id.artistName))
                .perform(typeText("Artist Name"), closeSoftKeyboard());
    }
    /**
     * Tests the functionality of the search button.
     */
    @Test
    public void testButtonClick() {
        onView(withId(R.id.buttonSearch))
                .perform(ViewActions.click());
    }
    /**
     * Tests scrolling in the RecyclerView.
     */
    @Test
    public void testRecyclerViewScroll() {
        onView(ViewMatchers.withId(R.id.saved_song_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(5));
    }
    /**
     * Tests entering an artist name in the EditText and verifies if it's displayed correctly.
     */
    @Test
    public void testEnterArtistName() {
        String artistName = "Artist Name";
        onView(withId(R.id.artistName)).perform(typeText(artistName), closeSoftKeyboard());
        onView(withId(R.id.artistName)).check(matches(withText(artistName)));
    }

    /**
     * Tests if clicking the "About" button shows the appropriate AlertDialog.
     */
    @Test
    public void testAlertDialogShow() {
        onView(withId(R.id.AboutSongSearch))
                .perform(ViewActions.click());
        onView(withText(R.string.songhelp))
                .check(matches(withText(R.string.songhelp)));
    }
}
