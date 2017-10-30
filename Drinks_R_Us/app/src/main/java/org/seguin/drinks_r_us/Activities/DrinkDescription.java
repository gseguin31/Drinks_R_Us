package org.seguin.drinks_r_us.Activities;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.seguin.drinks_r_us.Adapters.CreateDrink_IngredientAdapter;
import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.UserDrink;
import org.seguin.drinks_r_us.Models.Users;

import org.seguin.drinks_r_us.R;
import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.SingletonServeur;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkDescription extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    Users currentUser;
    int currentUserid;
    ServiceServeur serveurMock;
    Drinks currentDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_description);

        serveurMock = SingletonServeur.getInstance().serveur;

        final Intent currentIntent = getIntent();
        //get current drink passer dans intent
        currentDrink = (Drinks)currentIntent.getSerializableExtra("currentDrink");

        //Adapter du listview d'ingredient (On peux utiliser le meme que le createDrink activity car ca revient a le meme genre de list)
        ArrayList<Ingredients> listIngr = currentDrink.getListIngredients();
        CreateDrink_IngredientAdapter adapter = new CreateDrink_IngredientAdapter(this, R.layout.lv_createdrink_ingredient_item);
        ListView lvIngr = (ListView)findViewById(R.id.lv_drinkDescription_ingredients);
        lvIngr.setAdapter(adapter);
        adapter.addAll(listIngr);

        //Ajouter les info du drink dans les textview correspondant
        TextView tvNom = (TextView)findViewById(R.id.tvDescripNom);
        TextView tvInstruction = (TextView)findViewById(R.id.tvDescripInstruction);

        tvNom.setText(currentDrink.getName());
        tvInstruction.setText(currentDrink.getInstruction());

        //Button Listeners
        Button btnFav = (Button)findViewById(R.id.btnFavDrink);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Drinks> myFavs = new ArrayList<Drinks>();
                serveurMock.getMyFavs(currentUserid).enqueue(new Callback<ArrayList<Drinks>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Drinks>> call, Response<ArrayList<Drinks>> response) {
                        if (response.isSuccessful()){
                            myFavs.addAll(response.body());

                            if (myFavs.size() < 1){
                                addDrinkInFavs();
                            }
                            else{
                                //Verifier que le user na pas deja ce drink dans ses favoris
                                for (Drinks d :
                                        myFavs) {
                                    if (d.getId() == currentDrink.getId()){
                                        Toast.makeText(DrinkDescription.this, "Déjà dans les favoris", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        addDrinkInFavs();
                                    }
                                }
                            }
                        }
                        else{
                            Log.i("Retrofit", "code d'erreur est " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Drinks>> call, Throwable t) {

                    }
                });
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
                        serveurMock.logoutUser(currentUserid).enqueue(new Callback<Users>() {
                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (response.isSuccessful()){
                                    Intent intentLogin = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intentLogin);
                                }
                                else{
                                    Log.i("Retrofit", "code d'erreur est " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {

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

    public void addDrinkInFavs(){
        UserDrink userDrink = new UserDrink();
        userDrink.drink = currentDrink;
        userDrink.user = currentUser;
        serveurMock.addMyFavs(userDrink).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(DrinkDescription.this, "Ajouté aux favoris", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("currentUser", currentUserid);
                    startActivity(i);
                }
                else{
                    Log.i("Retrofit", "code d'erreur est " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
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
