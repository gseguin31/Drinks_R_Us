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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.seguin.drinks_r_us.Adapters.CreateDrink_IngredientAdapter;
import org.seguin.drinks_r_us.Adapters.MainActivity_DrinkAdapter;
import org.seguin.drinks_r_us.Models.Drinks;
import org.seguin.drinks_r_us.Models.Ingredients;
import org.seguin.drinks_r_us.Models.UserDrink;
import org.seguin.drinks_r_us.Models.Users;

import org.seguin.drinks_r_us.R;
import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.SingletonServeur;
import org.w3c.dom.Text;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    ServiceServeur serveurMock;
    int currentUserid;
    Users currentUser;
    public static Bus bus = new Bus();
    MainActivity_DrinkAdapter adapter;
    ArrayList<Drinks> allDrinksList;
    ProgressDialog progressD;
    Boolean isLanscape;
    Drinks currentDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        serveurMock = SingletonServeur.getInstance().serveur;

        isLanscape = findViewById(R.id.landscape) != null;


        //Button Listeners
        Button btnAjouterDrink = (Button)findViewById(R.id.btnAddDrink);
        btnAjouterDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createDrinkIntent = new Intent(getApplicationContext(), CreateDrink.class);
                createDrinkIntent.putExtra("currentUser", currentUserid);
                startActivity(createDrinkIntent);
            }
        });


        //ListView Adapter
        allDrinksList = new ArrayList<>();
        // show progressBar
        progressD = ProgressDialog.show(MainActivity.this, "Veuillez patienter",
                "Attente de réponse du serveur", true);
        serveurMock.getAllDrinks().enqueue(new Callback<ArrayList<Drinks>>() {
            @Override
            public void onResponse(Call<ArrayList<Drinks>> call, Response<ArrayList<Drinks>> response) {
                if (response.isSuccessful()){
                    progressD.dismiss();
                    allDrinksList.addAll(response.body());
                    refreshMainActivityListView(response.body());
                }
                else{
                    Log.i("Retrofit", "code d'erreur est " + response.code());
                    progressD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Drinks>> call, Throwable t) {
                progressD.dismiss();
            }
        });
        adapter = new MainActivity_DrinkAdapter(this, R.layout.lv_main_drinks_item, allDrinksList);
        ListView lvDrinks = (ListView)findViewById(R.id.lvMain_Drinks);
        if (isLanscape){
            lvDrinks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        lvDrinks.setAdapter(adapter);

        //hide ui items of drink details if no drink is selected in landscape mode
        if (isLanscape){
            Button btnfav = (Button)findViewById(R.id.btnFavDrink);
            TextView txtIngr = (TextView)findViewById(R.id.tvIngredient_Landscape);
            TextView txtInstr = (TextView)findViewById(R.id.tvInstruction_Landscape);
            btnfav.setVisibility(View.INVISIBLE);
            txtIngr.setVisibility(View.INVISIBLE);
            txtInstr.setVisibility(View.INVISIBLE);

            //add btnfav onclicklistener
            btnfav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<Drinks> myFavs = new ArrayList<Drinks>();
                    // show progressBar
                    progressD = ProgressDialog.show(MainActivity.this, "Veuillez patienter",
                            "Attente de réponse du serveur", true);
                    serveurMock.getMyFavs(currentUserid).enqueue(new Callback<ArrayList<Drinks>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Drinks>> call, Response<ArrayList<Drinks>> response) {
                            if (response.isSuccessful()){
                                myFavs.addAll(response.body());
                                progressD.dismiss();
                                if (myFavs.size() < 1){
                                    addDrinkInFavs();
                                }
                                else{
                                    //Verifier que le user na pas deja ce drink dans ses favoris
                                    for (Drinks d :
                                            myFavs) {
                                        if (d.getId() == currentDrink.getId()){
                                            Toast.makeText(MainActivity.this, "Déjà dans les favoris", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            addDrinkInFavs();
                                        }
                                    }
                                }
                            }
                            else{
                                Log.i("Retrofit", "code d'erreur est " + response.code());
                                progressD.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Drinks>> call, Throwable t) {
                            progressD.dismiss();
                        }
                    });
                }
            });
        }

        //listener du listview pour ouvrir lactivity de detail dun drink
        if (isLanscape){
            lvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Button btnfav = (Button)findViewById(R.id.btnFavDrink);
                    TextView txtIngr = (TextView)findViewById(R.id.tvIngredient_Landscape);
                    TextView txtInstr = (TextView)findViewById(R.id.tvInstruction_Landscape);
                    btnfav.setVisibility(View.VISIBLE);
                    txtIngr.setVisibility(View.VISIBLE);
                    txtInstr.setVisibility(View.VISIBLE);

                    currentDrink = allDrinksList.get(position);
                    //Adapter du listview d'ingredient (On peux utiliser le meme que le createDrink activity car ca revient a le meme genre de list)
                    ArrayList<Ingredients> listIngr = currentDrink.getListIngredients();
                    CreateDrink_IngredientAdapter adapter = new CreateDrink_IngredientAdapter(MainActivity.this, R.layout.lv_createdrink_ingredient_item);
                    ListView lvIngr = (ListView)findViewById(R.id.lv_drinkDescription_ingredients);
                    lvIngr.setAdapter(adapter);
                    adapter.addAll(listIngr);

                    TextView tvNom = (TextView)findViewById(R.id.tvDescripNom);
                    TextView tvInstruction = (TextView)findViewById(R.id.tvDescripInstruction);

                    tvNom.setText(currentDrink.getName());
                    tvInstruction.setText(currentDrink.getInstruction());
                }
            });
        }
        else {
            lvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent DrinkDescIntent = new Intent(getApplicationContext(), DrinkDescription.class);
                    DrinkDescIntent.putExtra("currentUser", currentUserid);
                    DrinkDescIntent.putExtra("currentDrink", allDrinksList.get(position));
                    startActivity(DrinkDescIntent);
                }
            });
        }



        //Menu de navigation
        NavigationView navView = (NavigationView)findViewById(R.id.my_navigation_view);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
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
                        progressD = ProgressDialog.show(MainActivity.this, "Veuillez patienter",
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
                                progressD.dismiss();
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


    private void refreshMainActivityListView(List<Drinks> list){
        ListView lv = (ListView)findViewById(R.id.lvMain_Drinks);
        adapter.clear();
        adapter.addAll(list);
        lv.setAdapter(adapter);
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
                progressD.dismiss();
            }
        });
    }

    public void addDrinkInFavs(){
        UserDrink userDrink = new UserDrink();
        userDrink.drink = currentDrink;
        userDrink.user = currentUser;
        // show progressBar
        progressD = ProgressDialog.show(MainActivity.this, "Veuillez patienter",
                "Attente de réponse du serveur", true);
        serveurMock.addMyFavs(userDrink).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Ajouté aux favoris", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("currentUser", currentUserid);
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
    }
}
