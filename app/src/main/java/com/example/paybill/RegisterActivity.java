
        package com.example.paybill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText txtFirstname, txtLastname, txtPhone, txtAddress, txtEmail, txtPassword;
    private RadioButton rbFemme, rbHomme;
    private Button btnRegister;
    private TextView responseTxt;
    private String firstname, lastname, phone, addresss, email, password, genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //matcher les variables avec les interfaces
        txtFirstname = findViewById(R.id.txtFirstName);
        txtLastname = findViewById(R.id.txtLastName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        rbFemme = findViewById(R.id.rbFemme);
        rbHomme = findViewById(R.id.rbFemme);
        btnRegister = findViewById(R.id.btnSave);
        responseTxt = findViewById(R.id.responseTextRegister);

        //ecouter du click sur le bouton register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                //startActivity(intent);
                //recuperer les valeurs des champs
                firstname = txtFirstname.getText().toString().trim();
                lastname = txtLastname.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                addresss = txtAddress.getText().toString().trim();
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                //on efface le contenue
                genre = "";
                if (rbFemme.isChecked()) {
                    genre = "F";
                }
                if (rbHomme.isChecked()) {
                    genre = "H";
                }

                //verifier que les champs sont vides
                if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || addresss.isEmpty()
                        || email.isEmpty() || password.isEmpty() || genre.isEmpty()) {
                    String message = getString(R.string.error_fields);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    responseTxt.setText(message);
                } else {
                    registerAPI();
                    //les information sont bonnes
                    //registerApi()
                    //System.out.println("je m'appelle " + firstname);
                    //String resume = firstname+"\n\n" +lastname+"\n\n"+phone+"\n\n" +addresss+"\n\n" +email+ password+"\n\n"+ genre+"\n\n" ;
                    //Toast.makeText(getApplicationContext(), resume, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void registerAPI() {
        String url = "https://paybill-api.herokuapp.com/register";
        OkHttpClient client = new OkHttpClient();
        //creation de la requette
        //Request request = new Request()
        //Create a JSONObject with the data to be sent to the server


        try {
            JSONObject jo = new JSONObject()
                    .put("lastname", lastname)
                    .put("firstname", firstname)
                    .put("email", email)
                    .put("gender", genre)
                    .put("phone", phone)
                    .put("address", addresss)
                    .put("password", password)
                    .put("type", "simple");

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
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        String status = jo.getString("status");
                        //connexion reussi
                        if (status.equals("Successfully")){
                            //on redirige vers la page d'accueil
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            //intent.putExtra("LOGIN",login);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = getString(R.string.error_parameters);
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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