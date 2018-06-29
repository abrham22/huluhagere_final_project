
package ethio.habesha.huluagerie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import java.util.ArrayList;
import java.util.Random;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RoadMapTest {


    private static final String EMAIL = "guest@freeworld.com";

    @Rule
    public IntentsTestRule<RoadMapActivity> rule = new IntentsTestRule<>(
            RoadMapActivity.class, true, false);

    private IdlingResource mIdlingResource;

    public void registerIdlingResource() {
        mIdlingResource = rule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void launchWithIntent() {

        DataController.Roadmap roadmap = new DataController.Roadmap();
        ArrayList<DataController.Activity> activities = new ArrayList<>();
        for(String location: new String[]{"Bahirdar", "Hawassa", "Addis Ababa"}) {
            DataController.Activity activity = new DataController.Activity();
            DataController.Site site = new DataController.Site();
            site.name = location;
            activity.site = site;
            activities.add(activity);
        }
        roadmap.name = "Random RoadMap";
        roadmap.activities = activities;
        Intent intent = new Intent();
        intent.putExtra("is_new", true);
        intent.putExtra("roadmap", roadmap);
        rule.launchActivity(intent);
        registerIdlingResource();

    }


    @Test
    public void testSaveRoadMap() {

        Intent intent = new Intent();
        intent.putExtra("email", EMAIL);

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, intent);
        intending(hasComponent(hasShortClassName(".LoginActivity")))
                .respondWith(result);

        onView(withId(R.id.save_remove)).perform(click());

        Random rand = new Random();
        onView(withId(R.id.road_map_name)).check(matches(isDisplayed()))
                .perform(typeText("Random"+rand.nextInt(1000)+" Road Map"),
                closeSoftKeyboard());

        onView(withText("OK")).perform(click());

        // check the outgoing intent
        intended(allOf(
                hasComponent(SavedRoadmapActivity.class.getName()),
                hasExtra("email", EMAIL)
        ));

    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

}