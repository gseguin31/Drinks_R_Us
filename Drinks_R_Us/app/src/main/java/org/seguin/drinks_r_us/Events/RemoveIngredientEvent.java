package org.seguin.drinks_r_us.Events;

import org.seguin.drinks_r_us.Models.Ingredients;

/**
 * Created by Gabriel on 08/09/2017.
 */

public class RemoveIngredientEvent {

    private Ingredients ing;

    public RemoveIngredientEvent(Ingredients e) {
        this.ing = e;
    }

    public Ingredients getIngredient(){
        return ing;
    }
}
