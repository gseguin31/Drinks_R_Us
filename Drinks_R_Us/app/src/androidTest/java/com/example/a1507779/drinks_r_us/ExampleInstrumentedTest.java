package com.example.a1507779.drinks_r_us;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.EmailPassword;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.RetroFitUtils;
import org.seguin.drinks_r_us.Models.Users;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
       Context appContext = InstrumentationRegistry.getTargetContext();
        //Response<Users> resp = RetroFitUtils.get().getUserById(2).execute();
        //Users user = resp.body();
        assertEquals("com.example.a1507779.drinks_r_us", appContext.getPackageName());
    }

    @Test
    public void createUserOK() throws Exception {
        EmailPassword cred = new EmailPassword();
        cred.email = "user1";
        cred.password = "pass1";

        Response<Users> resp = RetroFitUtils.get().createUser(cred).execute();
        Users user = resp.body();

        assertEquals("user1", RetroFitUtils.get().getUserById(0).execute().body().getUsername());
    }

    @Test
    public void createDrinkOK() throws Exception {
        EmailPassword cred = new EmailPassword();
        cred.email = "user1";
        cred.password = "pass1";
        Response<Users> resp = RetroFitUtils.get().createUser(cred).execute();
        Users user = resp.body();

        Ingredients ingredients1 = new Ingredients("ingr1");
        Ingredients ingredients2 = new Ingredients("ingr2");
        ArrayList<Ingredients> list = new ArrayList<>();
        list.add(ingredients1);
        list.add(ingredients2);
        Drinks drink = new Drinks("DrinkTest", "Instructions", list, 0);

        Response<String> respDrink = RetroFitUtils.get().createDrink(drink).execute();
        String drinkresp = respDrink.body();

        //assertEquals("DrinkTest", RetroFitUtils.get().getDrinkById(0).execute().body().getName());
    }
}
