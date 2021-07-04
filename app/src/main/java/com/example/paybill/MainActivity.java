
        package com.example.paybill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText txtLogin, txtPassword ;
    private Button btnConnect , btnSignUp ;
    private TextView responseTxt;
    private String login , password ;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnect = findViewById(R.id.btnConnect);
        btnSignUp = findViewById(R.id.btnSignUp);


        //appui sur le bouton s'inscrire
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //naviguer vers l'inscription
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //appui sur le bouton se connecter
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = txtLogin.getText().toString().trim();
                password = txtPassword.getText().toString().trim();

                if(validateLogin(login,password)){
                    //Intent intent = new Intent(MainActivity.this ,UserActivity.class);
                    //intent.putExtra("user_id",1);
                    //startActivity(intent);
                    login();
                }
            }
        });

    }

    private Boolean validateLogin(String login , String password){
        if(login == null || password.trim().length() == 0){
            Toast.makeText(this, getString(R.string.error_fields), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static User getUser() {
        return user;
    }

    private void login(){
        String url = "https://paybill-api.herokuapp.com/login";
        OkHttpClient client = new OkHttpClient();
        //creation de la requette
        //Request request = new Request()
        //Create a JSONObject with the data to be sent to the server

        try {
            JSONObject jo = new JSONObject()
                    .put("login", login)
                    .put("password", password);

            //parse json to json string
            String jsonString = jo.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = getString(R.string.error_connection);
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            responseTxt.setText(message);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //recuperer le json
                        JSONObject jo = new JSONObject(response.body().string());
                        //recuperer la value du json
                        //JSONObject user = jo.getJSONObject("user");
                        String status = jo.getString("status");
                        //connexion reussi
                        if (status.equals("Successfully")){
                            // fetch JSONObject named employee
                            JSONObject userAPI = jo.getJSONObject("user");
                            //creer un objet user
                            int id = userAPI.getInt("id");
                            String lastname = userAPI.getString("lastname");
                            String firstname = userAPI.getString("firstname");
                            String email = userAPI.getString("email");
                            String gender = userAPI.getString("gender");
                            String phone = userAPI.getString("phone");
                            String address = userAPI.getString("address");

                            user = new User(id,lastname,firstname,phone,email,address,gender);
                            //aller  vers l'ecran designe
                            Intent intent = new Intent(MainActivity.this,UserActivity.class);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = getString(R.string.error_parameters);
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    responseTxt.setText(message);
                                }
                            });
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}