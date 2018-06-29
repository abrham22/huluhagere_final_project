
package ethio.habesha.huluagerie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class SiteTest {


    @Rule
    public IntentsTestRule<AddSiteActivity> rule = new IntentsTestRule<>(
            AddSiteActivity.class, true, false);

    private IdlingResource mIdlingResource;

    public void registerIdlingResource() {
        mIdlingResource = rule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    private DataController.Site getSite() {

        Random rand = new Random();

        DataController.Site site = new DataController.Site();
        site.name = "Random"+rand.nextInt(1000)+" City";
        site.address = new DataController.Address();
        site.address.city = site.name;

        site.site_services = new ArrayList<>();
        DataController.Service service_one = new DataController.Service();
        DataController.Service service_two = new DataController.Service();
        service_one.name = "Dining";
        service_two.name = "Hiking";
        site.site_services.add(service_one);
        site.site_services.add(service_two);

        site.artifacts = new ArrayList<>();
        DataController.Artifact artifact_one = new DataController.Artifact();
        DataController.Artifact artifact_two = new DataController.Artifact();
        artifact_one.name = "Castles";
        artifact_two.name = "Churches";
        site.artifacts.add(artifact_one);
        site.artifacts.add(artifact_two);

        return site;
    }

    @Before
    public void launchWithIntent(boolean update, boolean site) {

        Intent intent = new Intent();
        intent.putExtra("update", update);
        if(site)
            intent.putExtra("site", getSite());
        rule.launchActivity(intent);
        registerIdlingResource();

    }


    @Test
    public void testAddSite() {

        launchWithIntent(false, false);

        DataController.Site site = getSite();

        onView(withId(R.id.site_name))
                .perform(typeText(site.name), closeSoftKeyboard());
        onView(withId(R.id.service1))
                .perform(typeText(site.site_services.get(0).name), closeSoftKeyboard());
        onView(withId(R.id.service2))
                .perform(typeText(site.site_services.get(1).name), closeSoftKeyboard());
        onView(withId(R.id.artifact1))
                .perform(typeText(site.artifacts.get(0).name), closeSoftKeyboard());
        onView(withId(R.id.artifact2))
                .perform(typeText(site.artifacts.get(1).name), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        // check the outgoing intent
        intended(allOf(
                hasComponent(SitesActivity.class.getName()),
                hasExtra("site", site)
        ));

    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

}