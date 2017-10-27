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
import android.widget.TextView;

import com.example.a1507779.drinks_r_us.Models.Drinks;
import com.example.a1507779.drinks_r_us.Models.Ingredients;
import com.example.a1507779.drinks_r_us.R;

import java.util.List;

/**
 * Created by 1507779 on 2017-09-05.
 */

public class MainActivity_DrinkAdapter extends ArrayAdapter<Drinks> {

    List<Drinks> listDrinks;
    Context context;
    int resource;


    public MainActivity_DrinkAdapter(@NonNull Context pContext, @LayoutRes int pResource, @NonNull List<Drinks> pObjects) {
        super(pContext, pResource, pObjects);

        this.listDrinks = pObjects;
        this.context = pContext;
        this.resource = pResource;
    }


    //Ceci est la methode qui est appeler a repetition selon le nombre d'item dans la liste. le parametre position determine la List[position]
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(resource, parent, false);

        Drinks drink = listDrinks.get(position);

        //Place le nom du drink
        TextView tvDrinkName = (TextView) v.findViewById(R.id.lv_item_DrinkName);
        tvDrinkName.setText(drink.Name);

        //Place les ingredients du drink
        TextView tvDrinkIngredients = (TextView)v.findViewById(R.id.lv_item_Ingredients);
        String ingrStrList = "";
        int cpt = 1;
        for (Ingredients ingr :
                drink.getListIngredients()) {
            //lorsque rendu au dernier item ne pas placer de virgule
            if (cpt != drink.getListIngredients().size()){
                ingrStrList = ingrStrList + (ingr.Nom + ", ");
            }
            else{
                ingrStrList = ingrStrList + (ingr.Nom);
            }
            cpt++;
        }
        tvDrinkIngredients.setText(ingrStrList);

        return v;
    }
}
