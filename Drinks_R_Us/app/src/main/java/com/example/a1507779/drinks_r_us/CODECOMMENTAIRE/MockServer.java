package com.example.a1507779.drinks_r_us.CODECOMMENTAIRE;

import com.example.a1507779.drinks_r_us.Exceptions.BadDrinkId;
import com.example.a1507779.drinks_r_us.Exceptions.BadIngredientId;
import com.example.a1507779.drinks_r_us.Exceptions.BadUserId;
import com.example.a1507779.drinks_r_us.Exceptions.DrinkNotFound;
import com.example.a1507779.drinks_r_us.Exceptions.IngredientExistException;
import com.example.a1507779.drinks_r_us.Exceptions.IngredientNotFoundException;
import com.example.a1507779.drinks_r_us.Exceptions.UsernameNotFound;
import com.example.a1507779.drinks_r_us.Models.Drinks;
import com.example.a1507779.drinks_r_us.Models.Ingredients;
import com.example.a1507779.drinks_r_us.Models.Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 1507779 on 2017-09-05.
 */

public class MockServer implements Serializable {

    private ArrayList<Users> listUsers;
    private ArrayList<Drinks> listDrink;
    private ArrayList<Ingredients>listIngredients;


    public MockServer(){
        listUsers = new ArrayList<Users>();
        listDrink = new ArrayList<Drinks>();
        listIngredients = new ArrayList<Ingredients>();
        generateRandomIngredients();
        sortIngredientList();
    }
    

    //region User Related

    public Users getUserByName(String pUsername) throws UsernameNotFound{
        for (Users u:
             listUsers) {
            if (u.getUsername().equals(pUsername)){
                return u;
            }
        }
        throw new UsernameNotFound();
    }

    public Users getUserById(int pid)throws BadUserId{
        if (pid > listUsers.size() || pid < 0)
            throw new BadUserId();
        Users user = null;
        user = listUsers.get(pid);
        return user;
    }

    public void addMyDrink(Drinks drink, Users user){
        user.getMyDrinks().add(drink);
    }

    public void addMyFav(Drinks drink, Users user){
        user.getMyFavs().add(drink);
    }

    public boolean verifyCredentials(String pUsername, String pPassword){
        Users user = null;
        try{
            user = getUserByName(pUsername);
        }
        catch (UsernameNotFound e){
            return false;
        }

        if (user.getPassword().equals(pPassword))
            return true;
        else
            return false;
    }

    public void createUser(String pUsername, String pPassword){
        try{
            getUserByName(pUsername);
        }
        catch (UsernameNotFound e){
            //Doit etre lancer car on ne veux pas trouver un username qui correspond pour ne pas avoir des doublons username
            Users newUser = new Users(pUsername, pPassword);
            listUsers.add(newUser);
        }
        //sinon rien faire car on ne veux pas cree le user
    }

    public ArrayList<Drinks> getUserFavs(Users user){
        return user.getMyFavs();
    }

    public ArrayList<Drinks> getUserDrinks(Users user){
        return user.getMyDrinks();
    }

    //endregion
    
    //region Drink Related

    public void createDrink(Drinks pDrink){
        listDrink.add(pDrink);
    }

    public ArrayList<Drinks> getAllDrinks(){
        return listDrink;
    }

    public Drinks getDrinkByName(String pName) throws DrinkNotFound{
        for (Drinks drink :
                listDrink) {
            if (drink.Name.equals(pName))
                return drink;
        }
        throw new DrinkNotFound();
    }

    public Drinks getDrinkById(int pId) throws BadDrinkId{
        if (pId > listDrink.size() || pId < 0)
            throw new BadDrinkId();
        return listDrink.get(pId);
    }
    
    //endregion
    
    //region Ingredient Related

    public void generateRandomIngredients(){
        try{
            createIngredient("Vodka");
            createIngredient("Rum");
            createIngredient("Jus dorange");
            createIngredient("Peche");
            createIngredient("Ananas");
            createIngredient("Citron");
            createIngredient("ingr1");
            createIngredient("ingr2");
            createIngredient("ingr3");
            createIngredient("ingr4");
            createIngredient("ingr5");
            createIngredient("ingr6");
            createIngredient("ingr7");
            createIngredient("ingr8");
            createIngredient("ingr9");
            createIngredient("ingr10");
            createIngredient("ingr11");
            createIngredient("ingr12");
            createIngredient("ingr13");
            createIngredient("ingr14");
            createIngredient("ingr15");
            createIngredient("ingr16");
            createIngredient("ingr17");

        }
        catch (IngredientExistException e){

        }
    }

    public void createIngredient(String pName) throws IngredientExistException{
        for (Ingredients ingr:
             listIngredients) {
            if (ingr.Nom.equals(pName))
                throw new IngredientExistException();
        }
        Ingredients newIngredient = new Ingredients(pName);
        listIngredients.add(newIngredient);
    }
    
    public Ingredients getIngredientByName(String pName)throws IngredientNotFoundException{
        for (Ingredients i :
                listIngredients) {
            if (i.Nom.equals(pName))
                return i;
        }
        throw new IngredientNotFoundException();
    }
    
    public Ingredients getIngredientById(int pId) throws BadIngredientId{
        if (pId > listIngredients.size() || pId < 0)
            throw new BadIngredientId();
        return listIngredients.get(pId);
    }

    public ArrayList<Ingredients> getAllIngredients(){
        return listIngredients;
    }

    public void sortIngredientList(){
        Collections.sort(listIngredients, new Comparator<Ingredients>() {
                    public int compare(Ingredients v1, Ingredients v2) {
                        return v1.Nom.compareTo(v2.Nom);
                    }
        });
    }
    //endregion
}
