
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

import java.util.Random;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {


    private static final String USER_PASSWORD = "random";

    @Rule
    public IntentsTestRule<RegisterActivity> rule = new IntentsTestRule<>(
            RegisterActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = rule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testUserLogin() {

        String USER_EMAIL = "random"+ new Random().nextInt(10000) + "@gmail.com";

        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(USER_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(USER_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.rePassword))
                .perform(typeText(USER_PASSWORD), closeSoftKeyboard());

        // click submit
        onView(withId(R.id.register)).perform(click());

        // check for the toast
        onView(withText(containsString("registered"))).
                inRoot(withDecorView(
                        not(is(rule.getActivity().getWindow().getDecorView())))
                ).check(matches(isDisplayed()));

    }

    @Test
    public void testLoginRedirection() {

        // click register
        onView(withId(R.id.login)).perform(click());
        // check for the intent
        intended(hasComponent(LoginActivity.class.getName()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

}