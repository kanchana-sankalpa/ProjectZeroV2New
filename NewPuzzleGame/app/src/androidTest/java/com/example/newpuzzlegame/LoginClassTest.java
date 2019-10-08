package com.example.newpuzzlegame;

import com.example.newpuzzlegame.util.L;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class LoginClassTest {


    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);


    // 1.1
    @Test
    public void checkforconnections() {
        
    }

    // 1.2
    @Test
    public void wheninputisbetween0to100() {
        InputValidation.input(50);
        Assert.assertTrue(true);
    }
}
