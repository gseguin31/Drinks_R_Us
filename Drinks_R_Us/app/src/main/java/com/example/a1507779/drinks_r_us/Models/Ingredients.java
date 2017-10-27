package com.example.a1507779.drinks_r_us.Models;

import java.io.Serializable;

/**
 * Created by 1507779 on 2017-09-05.
 */

public class Ingredients implements Serializable{

    public String Nom;
    private static int IdCounter = 0;
    public final int Id;

    public Ingredients(String pNom){
        this.Nom = pNom;
        this.Id = IdCounter++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        final Ingredients other = (Ingredients)obj;
        if (other.Id != this.Id)
            return false;
        else
            return true;
    }
}
