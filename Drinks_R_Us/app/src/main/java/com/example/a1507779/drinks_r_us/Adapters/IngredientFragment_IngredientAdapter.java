package com.example.a1507779.drinks_r_us.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a1507779.drinks_r_us.Activities.CreateDrink;
import com.example.a1507779.drinks_r_us.Activities.MainActivity;
import com.example.a1507779.drinks_r_us.Events.AddIngredientEvent;
import com.example.a1507779.drinks_r_us.Events.RemoveIngredientEvent;
import com.example.a1507779.drinks_r_us.Models.Drinks;
import com.example.a1507779.drinks_r_us.Models.Ingredients;
import com.example.a1507779.drinks_r_us.R;

import java.util.List;

/**
 * Created by Gabriel on 07/09/2017.
 */

public class IngredientFragment_IngredientAdapter extends ArrayAdapter<Ingredients> {


    List<Ingredients> listIngredients;
    List<Ingredients> ingredientDejaChoisi;
    Context context;
    int resource;

    public IngredientFragment_IngredientAdapter(@NonNull Context pContext, @LayoutRes int pResource, @NonNull List<Ingredients> pObjects, @NonNull List<Ingredients> pIngredientDejaChoisi) {
        super(pContext, pResource, pObjects);

        this.listIngredients = pObjects;
        this.ingredientDejaChoisi = pIngredientDejaChoisi;
        this.context = pContext;
        this.resource = pResource;
    }


    //Ceci est la methode qui est appeler a repetition selon le nombre d'item dans la liste. le parametre position determine la List[position]
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(resource, parent, false);

        final Ingredients ingredient = listIngredients.get(position);
        TextView tvIngredient = (TextView)v.findViewById(R.id.tv_frag_ingredient);
        tvIngredient.setText(ingredient.Nom);


        //gerer les ingredient a ajouter dans le listview de CreateDrink
        final CheckBox checkBox = (CheckBox)v.findViewById(R.id.ingredient_checkbox);
        if (ingredientDejaChoisi.contains(ingredient)){
            //Cocher les ingredient qui on deja ete choisi
            checkBox.setChecked(true);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pour une raison c'est linverse de la logique...
                if (!checkBox.isChecked()){
                    MainActivity.bus.post(new RemoveIngredientEvent(ingredient));
                }
                else {
                    MainActivity.bus.post(new AddIngredientEvent(ingredient));
                }
            }
        });

        return v;
    }
}
