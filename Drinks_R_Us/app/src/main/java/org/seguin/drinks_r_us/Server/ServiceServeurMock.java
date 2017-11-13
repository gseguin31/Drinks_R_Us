package org.seguin.drinks_r_us.Server;

import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.EmailPassword;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.Token;
import org.seguin.drinks_r_us.Models.UserDrink;
import org.seguin.drinks_r_us.Models.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by Gabriel on 15/09/2017.
 */

public class ServiceServeurMock implements ServiceServeur {

    BehaviorDelegate<ServiceServeur> delegate;
    private Users user;
    private Ingredients ing;
    private ArrayList<Ingredients> listIng;
    private Drinks drink;


    public ServiceServeurMock(BehaviorDelegate<ServiceServeur> delegate) {
        this.delegate = delegate;
        this.user = new Users("Henry", "pass");
        this.ing = new Ingredients("Jus d'orange");
        this.listIng = new ArrayList<>();
        this.listIng.add(ing);
        this.drink = new Drinks("Chunky","Verser 1 ounce de jus d'orange dans un verre", listIng, 0);
        user.addMyDrink(drink);
        user.addMyFav(drink);
    }

    //region User Related

    @Override
    public Call<Users> getUserById(int id) {
        return delegate.returningResponse(user).getUserById(id);
    }

    @Override
    public Call<Users> logoutUser(int id) {
        return delegate.returningResponse(user).getUserById(id);
    }

    @Override
    public Call<ArrayList<Drinks>> getMyDrinks(int id){
        return delegate.returningResponse(user.getMyDrinks()).getMyDrinks(id);
    }

    @Override
    public Call<ArrayList<Drinks>> getMyFavs(int id){
        return delegate.returningResponse(user.getMyFavs()).getMyFavs(id);
    }

    @Override
    public Call<String> addMyDrinks(UserDrink drink){
        return delegate.returningResponse(drink).addMyDrinks(drink);
    }

    @Override
    public Call<String> addMyFavs(UserDrink drink){
        return delegate.returningResponse(drink).addMyFavs(drink);
    }

    @Override
    public Call<Token> verifyCredentials(EmailPassword cred) {
        return delegate.returningResponse(user).verifyCredentials(cred);
    }

    @Override
    public Call<Users> createUser(EmailPassword cred) {
        return delegate.returningResponse(user).createUser(cred);
    }

    //endregion

    //region Drink Related
    @Override
    public Call<String> createDrink(Drinks drink) {
        return delegate.returningResponse(drink).createDrink(drink);
    }

    @Override
    public Call<ArrayList<Drinks>> getAllDrinks() {
        ArrayList<Drinks> e = new ArrayList<>();
        e.add(drink);
        return delegate.returningResponse(e).getAllDrinks();
    }

    /*
    @Override
    public Call<ArrayList<Drinks>> getDrinkByName(String name) {
        ArrayList<Drinks> d = new ArrayList<>();
        d.add(drink);
        return delegate.returningResponse(d).getDrinkByName(name);
    }

    @Override
    public Call<Drinks> getDrinkById(int id) {
        return delegate.returningResponse(drink).getDrinkById(id);
    }
    */
    //endregion

    //region Ingredient Related

    @Override
    public Call<String> createIngredient(Ingredients ingr) {

        return delegate.returningResponse(ing).createIngredient(ingr);
    }

    /*
    @Override
    public Call<Ingredients> getIngredientByName(String name) {
        return delegate.returningResponse(ing).getIngredientByName(name);
    }

    @Override
    public Call<Ingredients> getIngredientById(int id) {
        return delegate.returningResponse(ing).getIngredientById(id);
    }
    */

    @Override
    public Call<ArrayList<Ingredients>> getAllIngredients() {
        ArrayList<Ingredients> i = new ArrayList<>();
        i.add(ing);
        return delegate.returningResponse(i).getAllIngredients();
    }

    //endregion
}
