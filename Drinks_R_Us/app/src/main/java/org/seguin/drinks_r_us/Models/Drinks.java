package org.seguin.drinks_r_us.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 1507779 on 2017-09-05.
 */

public class Drinks implements Serializable{

    public String Name;
    private ArrayList<Ingredients> listIngredients;
    public String Instruction;
    private int userId;
    private static int IdCounter = 0;
    private final int Id;

    public Drinks(String pName, String pInstruction, ArrayList<Ingredients> pIngredients, int pUserId){
        this.Id = IdCounter++;
        this.Name = pName;
        this.Instruction = pInstruction;
        this.listIngredients = new ArrayList<Ingredients>();
        this.userId = pUserId;
        for (Ingredients ingr :
                pIngredients) {
            listIngredients.add(ingr);
        }
    }

    public int getId(){
        return Id;
    }

    public ArrayList<Ingredients> getListIngredients(){
        return listIngredients;
    }

    public String getName(){
        return Name;
    }

    public String getInstruction(){
        return Instruction;
    }

    public int getUserId(){
        return userId;
    }
}
