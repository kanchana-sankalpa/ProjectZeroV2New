package com.example.newpuzzlegame;

import android.content.Intent;
import android.os.IBinder;
import android.service.autofill.Validator;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import androidx.test.espresso.Root;
import androidx.test.rule.ActivityTestRule;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class UserNameClassTest {

    @Rule
    public ActivityTestRule<UserName> UsernameActivityRule = new ActivityTestRule<>(UserName.class);

    @Test
    public void checkforemptyuserName() {


        onView(withId(R.id.login)).perform(click());
      onView(withText(R.string.name_please)).inRoot(new ToastMatcher())
               .check(matches(isDisplayed()));

    }


}

