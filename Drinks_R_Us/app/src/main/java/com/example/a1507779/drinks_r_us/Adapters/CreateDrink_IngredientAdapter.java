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

import com.example.a1507779.drinks_r_us.Models.Ingredients;
import com.example.a1507779.drinks_r_us.R;

import java.util.List;

/**
 * Created by Gabriel on 07/09/2017.
 */

public class CreateDrink_IngredientAdapter extends ArrayAdapter<Ingredients> {

    //List<Ingredients> listIngredients;
    Context context;
    int resource;

    public CreateDrink_IngredientAdapter(@NonNull Context pContext, @LayoutRes int pResource) {
        super(pContext, pResource);

        //this.listIngredients = pObjects;
        this.context = pContext;
        this.resource = pResource;
    }


    //Ceci est la methode qui est appeler a repetition selon le nombre d'item dans la liste. le parametre position determine la List[position]
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(resource, parent, false);

        Ingredients ingredient = this.getItem(position);

        TextView tvIngredient = (TextView)v.findViewById(R.id.tv_createDrink_ingredient);
        tvIngredient.setText(ingredient.Nom);

        return v;
    }
}
