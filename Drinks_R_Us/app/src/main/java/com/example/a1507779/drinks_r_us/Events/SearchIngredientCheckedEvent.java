package com.example.a1507779.drinks_r_us.Events;

import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Gabriel on 14/09/2017.
 */

public class SearchIngredientCheckedEvent {
    private CheckBox checkbox;

    public SearchIngredientCheckedEvent(CheckBox e) {
        this.checkbox = e;
    }

    public CheckBox getCheckbox(){
        return checkbox;
    }
}
