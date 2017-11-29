package org.seguin.drinks_r_us.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.seguin.drinks_r_us.Adapters.CreateDrink_IngredientAdapter;
import org.seguin.drinks_r_us.Events.AddIngredientEvent;
import org.seguin.drinks_r_us.Events.RemoveIngredientEvent;
import org.seguin.drinks_r_us.Events.UserFinishedAddingIngrEvent;
import org.seguin.drinks_r_us.Fragments.IngredientFragment;
import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.UserDrink;
import org.seguin.drinks_r_us.Models.Users;

import org.seguin.drinks_r_us.R;
import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.SingletonServeur;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDrink extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    ServiceServeur serveurMock;
    public static ArrayList<Ingredients> listIngrChoisi;
    CreateDrink_IngredientAdapter adapter;
    Bus bus;
    Users currentUser;
    int currentUserid;
    ProgressDialog progressD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drink);

        this.setTitle("Crée cocktail");

        bus = MainActivity.bus;

        serveurMock = SingletonServeur.getInstance().serveur;

        //Adapter du listview d'ingredient
        listIngrChoisi = new ArrayList<Ingredients>();
        adapter = new CreateDrink_IngredientAdapter(this, R.layout.lv_createdrink_ingredient_item);
        ListView lvIngr = (ListView)findViewById(R.id.lv_createDrinkActivity_Ingredients);
        lvIngr.setAdapter(adapter);
        adapter.clear();
        adapter.addAll(listIngrChoisi);

        //Buttons Listeners
        Button btnCreateDrink = (Button)findViewById(R.id.btnCreateDrink);
        btnCreateDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edNom = (EditText)findViewById(R.id.etCreateDrink_Nom);
                EditText edInstructions = (EditText)findViewById(R.id.etCreateDrink_Instructions);

                String edNomString = edNom.getText().toString().trim();
                String edInstructionsString = edInstructions.getText().toString().trim();


                //Verif l'integrite du drink avant de cree
                if(edNomString.isEmpty() || edNomString.length() == 0 || edNomString.equals("") || edNomString == null)
                {
                    Toast.makeText(CreateDrink.this, "Veillez saisir un nom", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edInstructionsString.isEmpty() || edInstructionsString.length() == 0 || edInstructionsString.equals("") || edInstructionsString == null)
                {
                    Toast.makeText(CreateDrink.this, "Veillez saisir un instruction", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (adapter.getCount() == 0){
                    Toast.makeText(CreateDrink.this, "Veillez saisir un ingrédient", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create drink
                Drinks newDrink = new Drinks(edNomString, edInstructionsString, listIngrChoisi, currentUser.getId());

                // show progressBar
                progressD = ProgressDialog.show(CreateDrink.this, "Veuillez patienter",
                        "Attente de réponse du serveur", true);

                serveurMock.createDrink(newDrink).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(CreateDrink.this, "Le cocktail a été crée", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("currentUser", currentUserid);
                            progressD.dismiss();
                            startActivity(i);
                        }
                        else{
                            Log.i("Retrofit", "code d'erreur est " + response.code());
                            progressD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressD.dismiss();
                    }
                });

                //add created drink in users myDrinks
                UserDrink userdrink = new UserDrink();
                userdrink.user = currentUser;
                userdrink.drink = newDrink;
                // show progressBar
                progressD = ProgressDialog.show(CreateDrink.this, "Veuillez patienter",
                        "Attente de réponse du serveur", true);
                serveurMock.addMyDrinks(userdrink).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            progressD.dismiss();
                        }
                        else {
                            Log.i("Retrofit", "d'erreur est " + response.body());
                            progressD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressD.dismiss();
                    }
                });
            }
        });


        Button btnAjouterIngredient = (Button)findViewById(R.id.btnAddIngredient);
        btnAjouterIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowFragmentIngredient();
            }
        });


        //Menu de navigation
        NavigationView navView = (NavigationView)findViewById(R.id.my_navigation_view);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.nav_item_MesCocktails:
                        Intent intentCock = new Intent(getApplicationContext(), MyDrinks.class);
                        intentCock.putExtra("currentUser", currentUserid);
                        startActivity(intentCock);
                        break;
                    case R.id.nav_item_Fav:
                        Intent intentFav = new Intent(getApplicationContext(), MyFavs.class);
                        intentFav.putExtra("currentUser", currentUserid);
                        startActivity(intentFav);
                        break;
                    case R.id.nav_item_Logout:
                        // show progressBar
                        progressD = ProgressDialog.show(CreateDrink.this, "Veuillez patienter",
                                "Attente de réponse du serveur", true);
                        serveurMock.logoutUser(currentUserid).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()){
                                    progressD.dismiss();
                                    Intent intentLogin = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intentLogin);
                                }
                                else{
                                    Log.i("Retrofit", "code d'erreur est " + response.code());
                                    progressD.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                        break;
                    case R.id.nav_item_Home:
                        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                        intentHome.putExtra("currentUser", currentUserid);
                        startActivity(intentHome);
                        break;
                    case R.id.nav_item_Create:
                        Intent intentCreate = new Intent(getApplicationContext(), CreateDrink.class);
                        intentCreate.putExtra("currentUser", currentUserid);
                        startActivity(intentCreate);
                        break;
                }

                return false;
            }
        });
        //listener du drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Ajouter username dans le header du drawer
        setUsernameInMenu();
    }

    //Click sur le hamburger ouvre le menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Sync letat du drawer si lecran a eu une rotation
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        toggle.syncState();
        super.onConfigurationChanged(newConfig);
    }

    private void createAndShowFragmentIngredient(){
        Bundle args = new Bundle();
        args.putSerializable("ingredientDejaChoisi", listIngrChoisi);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "Fragment");
    }

    public void refreshListView(){
        ListView lv = (ListView)findViewById(R.id.lv_createDrinkActivity_Ingredients);
        adapter.clear();
        adapter.addAll(listIngrChoisi);
        lv.setAdapter(adapter);
    }

    //Bus
    @Override
    protected void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    //Bus
    @Override
    protected void onResume() {
        bus.register(this);
        super.onResume();
    }


    @Subscribe
    public void reagirAddIngredientEvent(AddIngredientEvent ingr){
        listIngrChoisi.add(ingr.getIngredient());
    }

    @Subscribe
    public void reagirRemoveIngredientEvent(RemoveIngredientEvent ingr){
        listIngrChoisi.remove(ingr.getIngredient());
    }

    @Subscribe
    public void reagirUserFinishedAddingIngrEvent(UserFinishedAddingIngrEvent e){
        refreshListView();
    }

    public void setUsernameInMenu(){
        final Intent currentIntent = getIntent();
        currentUserid = (int)currentIntent.getSerializableExtra("currentUser");
        serveurMock.getUserById(currentUserid).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()){
                    currentUser = response.body();
                    NavigationView navView = (NavigationView)findViewById(R.id.my_navigation_view);
                    View headerView = (View)navView.getHeaderView(0);
                    TextView tvHeader = (TextView)headerView.findViewById(R.id.tvMenuHeader);
                    tvHeader.setText("Bonjour " + currentUser.getUsername());
                }
                else{
                    Log.i("Retrofit", "code d'erreur est " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });
    }
}
