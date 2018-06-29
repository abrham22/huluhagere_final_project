
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

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private static final String ADMIN_EMAIL = "root@gmail.com";
    private static final String ADMIN_PASSWORD = "root";

    private static final String USER_EMAIL = "guest@freeworld.com";
    private static final String USER_PASSWORD = "heni";

    @Rule
    public IntentsTestRule<LoginActivity> rule = new IntentsTestRule<>(
            LoginActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = rule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void testUserLogin() {

        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(USER_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(USER_PASSWORD), closeSoftKeyboard());

        // click submit
        onView(withId(R.id.login)).perform(click());

        // check for the toast
        onView(withText(R.string.login_success_toast)).
                inRoot(withDecorView(
                        not(is(rule.getActivity().
                                getWindow().getDecorView())))).
                check(matches(isDisplayed()));

    }

    @Test
    public void testAdminLogin() {

        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(ADMIN_EMAIL), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(ADMIN_PASSWORD), closeSoftKeyboard());

        // click submit
        onView(withId(R.id.login)).perform(click());

        // check for the intent
        intended(hasComponent(SitesListActivity.class.getName()));

    }

    @Test
    public void testRegisterRedirection() {

        // click register
        onView(withId(R.id.register)).perform(click());

        // check for the intent
        intended(hasComponent(RegisterActivity.class.getName()));

    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

}