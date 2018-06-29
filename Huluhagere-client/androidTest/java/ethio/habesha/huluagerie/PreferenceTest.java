
package ethio.habesha.huluagerie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import java.util.Random;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class PreferenceTest {


    @Rule
    public IntentsTestRule<PreferenceActivity> rule = new IntentsTestRule<>(
            PreferenceActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = rule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testRandomPreferences() {

        Random rand = new Random();

        int numDays = rand.nextInt(10);
        onView(withId(R.id.tourLength))
                .perform(typeText(""+numDays), closeSoftKeyboard());

        int[][] check_ids = new int[][] {
                {R.id.entertainment, R.id.culture, R.id.cuisine, R.id.fitness, R.id.nature},
                {R.id.castle, R.id.church, R.id.mosque, R.id.cave, R.id.museum},
                {R.id.addisababa, R.id.mekelle, R.id.hawassa, R.id.bahirdar, R.id.adama},
                {R.id.movies, R.id.swimming, R.id.hiking, R.id.pizza, R.id.gari}
        };

        int serviceId1, serviceId2;
        for(int i = 0; i < 4; i++) {
            serviceId1 = check_ids[i][rand.nextInt(5)];
            onView(withId(serviceId1)).perform(scrollTo(), click());
            do {
                serviceId2 = check_ids[i][rand.nextInt(5)];
            } while (serviceId1 == serviceId2);
            onView(withId(serviceId2)).perform(scrollTo(), click());
        }

        // click submit
        onView(withId(R.id.submit)).perform(scrollTo(), click());

        // check the outgoing intent
        intended(allOf(
                hasComponent(RoadMapActivity.class.getName()),
                hasExtra("is_new", true),
                hasExtraWithKey("roadmap")));

    }

    @Test
    public void testLoginRedirection() {

        // click register 
        onView(withId(R.id.login)).perform(click());
        // check for the intent 
        intended(hasComponent(LoginActivity.class.getName() ) );

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

}