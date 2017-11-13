package org.seguin.drinks_r_us.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.seguin.drinks_r_us.Models.Token;
import org.seguin.drinks_r_us.Models.Users;
import org.seguin.drinks_r_us.Models.EmailPassword;
import org.seguin.drinks_r_us.R;
import org.seguin.drinks_r_us.Server.ServiceServeur;
import org.seguin.drinks_r_us.Server.SingletonServeur;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    ServiceServeur serverMock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serverMock = SingletonServeur.getInstance().serveur;


        getSupportActionBar().setTitle("Drinks R Us");



        //Buttons Listeners
        Button btnLogin = (Button)findViewById(R.id.BtnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText)findViewById(R.id.etUsernameLogin);
                EditText etPassword = (EditText)findViewById(R.id.etPasswordLogin);
                EmailPassword credentials = new EmailPassword();
                credentials.email = etUsername.getText().toString();
                credentials.password = etPassword.getText().toString();

                if (credentials.email.length() < 1){
                    Toast.makeText(Login.this, "Veuillez saisir un nom d'utilisateur", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (credentials.password.length() < 1){
                    Toast.makeText(Login.this, "Veuillez saisir un mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                serverMock.verifyCredentials(credentials).enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()){
                            final Token token = response.body();
                            Intent logIntent = new Intent(getApplicationContext(), MainActivity.class);
                            logIntent.putExtra("currentUser", token.UserId);
                            startActivity(logIntent);
                        }
                        else{
                            Log.i("Retrofit", "code d'erreur est " + response.code());
                            Toast.makeText(Login.this, "Les informations saisie sont erroné", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {

                    }
                });


            }
        });

        Button btnCree = (Button)findViewById(R.id.BtnUserCreate);
        btnCree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText)findViewById(R.id.etUsernameLogin);
                EditText etPassword = (EditText)findViewById(R.id.etPasswordLogin);
                EmailPassword credentials = new EmailPassword();
                credentials.email = etUsername.getText().toString();
                credentials.password = etPassword.getText().toString();

                if (credentials.email.length() < 1){
                    Toast.makeText(Login.this, "Veuillez saisir un nom d'utilisateur", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (credentials.password.length() < 1){
                    Toast.makeText(Login.this, "Veuillez saisir un mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                serverMock.createUser(credentials).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()){
                            Users newUser = response.body();
                            Intent logIntent = new Intent(getApplicationContext(), MainActivity.class);
                            logIntent.putExtra("currentUser", newUser.getId());
                            startActivity(logIntent);
                        }
                        else{
                            Log.i("Retrofit", "code d'erreur est " + response.code());
                            Toast.makeText(Login.this, "Veuillez choisir un autre nom d'utilisateur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {

                    }
                });
            }
        });
    }
}
