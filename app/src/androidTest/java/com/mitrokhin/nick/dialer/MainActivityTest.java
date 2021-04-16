package com.mitrokhin.nick.dialer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by kolyan on 4/2/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Balance";
    }

    @Test
    public void showSearchBarAndTypeText() {
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        openContextualActionModeOverflowMenu();
        // Type text and then press the button.
        onView(withText("Search contacts"))
                .perform(click());
        onView(withId(R.id.etSearch))
                .perform(typeText(mStringToBetyped));
        onView(withId(R.id.etSearch))
                .check(matches(withText(mStringToBetyped)));
        onData(anything()).inAdapterView(withId(R.id.contactList)).atPosition(0).perform(click());
    }
}
