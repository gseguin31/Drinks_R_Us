package org.seguin.drinks_r_us.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import org.seguin.drinks_r_us.Activities.MainActivity;
import org.seguin.drinks_r_us.Activities.MyFavs;
import org.seguin.drinks_r_us.Adapters.IngredientFragment_IngredientAdapter;
import org.seguin.drinks_r_us.Events.UserFinishedAddingIngrEvent;
import org.seguin.drinks_r_us.Models.Ingredients;

import org.seguin.drinks_r_us.R;
import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.SingletonServeur;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gabriel on 07/09/2017.
 */

public class IngredientFragment  extends DialogFragment{

    ServiceServeur serverMock;
    List<Ingredients> ingredientDejaChoisi;
    IngredientFragment_IngredientAdapter adapter;
    ProgressDialog progressD;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.lv_fragment_ingredient, null);
        builder.setView(v);


        //retreive server from args in bundle
        Bundle args = getArguments();
        serverMock = SingletonServeur.getInstance().serveur;
        //retreive ingredient qui on deja ete choisi pour les mettre cocher dans le listview
        ingredientDejaChoisi = (ArrayList<Ingredients>)args.getSerializable("ingredientDejaChoisi");


        //Adapter du listview d'ingredient
        final ArrayList<Ingredients> allIngredientList = new ArrayList<>();
        // show progressBar
        progressD = ProgressDialog.show(getActivity(), "Veuillez patienter",
                "Attente de réponse du serveur", true);
        serverMock.getAllIngredients().enqueue(new Callback<ArrayList<Ingredients>>() {
            @Override
            public void onResponse(Call<ArrayList<Ingredients>> call, Response<ArrayList<Ingredients>> response) {
                if (response.isSuccessful()){
                    progressD.dismiss();
                    refreshMainActivityListView(v, response.body());
                }
                else{
                    Log.i("Retrofit", "code d'erreur est " + response.code());
                    progressD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Ingredients>> call, Throwable t) {
                progressD.dismiss();
            }
        });
        adapter = new IngredientFragment_IngredientAdapter(getActivity(), R.layout.lv_frag_ingredients_item, allIngredientList, ingredientDejaChoisi);
        ListView lvIngr = (ListView)v.findViewById(R.id.lv_createDrink_Ingredient);
        lvIngr.setAdapter(adapter);


        //Force user a clicker sur le ok pour fermer le dialog pour eviter que le listview dans CreateDrink ne refresh pas
        this.setCancelable(false);

        // Add action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.bus.post(new UserFinishedAddingIngrEvent());
                dismiss();
            }
        });
        return builder.create();
    }
    public void refreshMainActivityListView(View v,List<Ingredients> list){
        ListView lv = (ListView)v.findViewById(R.id.lv_createDrink_Ingredient);
        adapter.clear();
        adapter.addAll(list);
        lv.setAdapter(adapter);
    }
}
