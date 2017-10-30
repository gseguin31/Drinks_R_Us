package org.seguin.drinks_r_us.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 1507779 on 2017-09-05.
 */

public class Users implements Serializable{

    private static int IdCounter = 0;
    private final int Id;
    public  String username;
    public  String password;
    private ArrayList<Drinks> listMyDrinks;
    private ArrayList<Drinks> listMyFavs;

    public Users(String pUsername, String pPassword){
        this.Id = IdCounter++;
        this.username = pUsername;
        this.password = pPassword;

        listMyDrinks = new ArrayList<Drinks>();
        listMyFavs = new ArrayList<Drinks>();
    }

    public int getId(){
        return Id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public ArrayList<Drinks> getMyDrinks(){
        return listMyDrinks;
    }
    public ArrayList<Drinks> getMyFavs(){
        return listMyFavs;
    }

    public void addMyDrink(Drinks drink){
        listMyDrinks.add(drink);
    }

    public void addMyFav(Drinks drink){
        listMyFavs.add(drink);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        final Users other = (Users)obj;
        if (other.Id != this.Id)
            return false;
        else
            return true;
    }
}
