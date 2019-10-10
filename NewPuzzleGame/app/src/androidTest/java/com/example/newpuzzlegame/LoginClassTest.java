package com.example.newpuzzlegame;

import com.example.newpuzzlegame.util.L;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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


}
