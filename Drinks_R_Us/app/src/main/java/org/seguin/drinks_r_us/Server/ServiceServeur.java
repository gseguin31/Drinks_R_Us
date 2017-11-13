package org.seguin.drinks_r_us.Server;

import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.EmailPassword;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.Token;
import org.seguin.drinks_r_us.Models.UserDrink;
import org.seguin.drinks_r_us.Models.Users;

import java.util.ArrayList;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Gabriel on 15/09/2017.
 */

public interface ServiceServeur {

    //region User Related

    @GET("/api/Users/GetUser/{id}")
    Call<Users> getUserById(@Path("id") int id);

    @GET("/api/Users/Logout/{id}")
    Call<Users> logoutUser(@Path("id") int id);

    @GET("/api/Users/MyDrinks/{id}")
    Call<ArrayList<Drinks>> getMyDrinks(@Path("id") int id);

    @GET("/api/Users/MyFavs/{id}")
    Call<ArrayList<Drinks>> getMyFavs(@Path("id") int id);

    @POST("/api/Users/AddDrink")
    Call<String> addMyDrinks(@Body UserDrink drink);

    @POST("/api/Users/AddFav")
    Call<String> addMyFavs(@Body UserDrink drink);

    @POST("/api/Users/VerifyCredentials")
    Call<Token> verifyCredentials(@Body EmailPassword login);

    @POST("/api/Users/Create")
    Call<Users> createUser(@Body EmailPassword credentials);

    //endregion

    //region Drink Related

    @POST("/api/Drinks/Create")
    Call<String> createDrink(@Body Drinks drink);

    @GET("/api/Drinks/GetAllDrinks")
    Call<ArrayList<Drinks>> getAllDrinks();

    /*
    @GET("/api/Drinks/GetDrinkByName/{name}")
    Call<ArrayList<Drinks>> getDrinkByName(@Path("name") String name);

    @GET("/api/Drinks/GetDrinkById/{id}")
    Call<Drinks> getDrinkById(@Path("id") int id);

    */
    //endregion

    //region Ingredient Related

    @POST("/api/Ingredients/Create")
    Call<String> createIngredient(@Body Ingredients ingr);

    /*
    @GET("/api/Ingredients/GetIngredientByName/{name}")
    Call<Ingredients> getIngredientByName(@Path("name") String name);

    @GET("/apiIngredients/GetingredientById/{id}")
    Call<Ingredients> getIngredientById(@Path("id") int id);
    */

    @GET("/api/Ingredients/GetAllIngredient")
    Call<ArrayList<Ingredients>> getAllIngredients();

    //endregion
}
