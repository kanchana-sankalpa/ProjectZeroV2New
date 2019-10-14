package com.example.newpuzzlegame;

import android.content.Intent;

import com.example.newpuzzlegame.util.L;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginClassTest {


    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);


    // 1.1
    @Test
    public void checkforconnections() {

        onView(withId(R.id.signin_google)).perform(click());
        assertTrue(mActivityRule.getActivity().checkConnection());

    }


    @Test
    public void checkforopennextActivity() {

        onView(withId(R.id.signin_google)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(mActivityRule.getActivity().isFinishing());

    }




}
