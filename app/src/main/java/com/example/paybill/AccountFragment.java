
        package com.example.paybill;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AccountFragment extends Fragment {


    private TextView txtFirstname,txtLastname,txtPhone,txtAdress,txtGender,txtEmail,txtNumber,txtAmount;
    private String firstname, lastname,phone,adress,gender,email,number,account;
    private Handler handler = new Handler();
    private Button btnLogout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        txtFirstname = view.findViewById(R.id.txtFirstName);
        txtLastname = view.findViewById(R.id.txtLastName);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAdress = view.findViewById(R.id.txtAddress);
        txtGender = view.findViewById(R.id.txtSexe);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtNumber = view.findViewById(R.id.NumberAccount);
        txtAmount = view.findViewById(R.id.amount);
        btnLogout = view.findViewById(R.id.btnlogOut);

        //charge la methode pour recuperer les donnees
        getUserInfo();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getUser().setId(0);
                //Intent intent = new Intent(getActivity().get.this,MainActivity.class);
                //startActivity(intent);
            }
        });

        return view;
    }

    private void getUserInfo() {
        String url  = "https://paybill-api.herokuapp.com/users/"+MainActivity.getUser().getId();
        OkHttpClient client =  new OkHttpClient();
        Request request =  new Request.Builder().
                url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String message = getString(R.string.error_connection);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                //responseTxt.setText(message);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jo = new JSONObject(response.body().string());

                                JSONObject userAPI = jo.getJSONObject("user");

                                String lastname = userAPI.getString("lastname");
                                String firstname = userAPI.getString("firstname");
                                String phone = userAPI.getString("phone");
                                String email = userAPI.getString("email");
                                String address = userAPI.getString("address");
                                String genre = userAPI.getString("gender");

                                System.out.println(userAPI);


                                //account info
                                JSONObject accountUser = userAPI.getJSONObject("account");
                                System.out.println(accountUser);


                                String number = accountUser.getString("number");
                                String balance = accountUser.getString("balance");

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtFirstname.setText(firstname);
                                        txtLastname.setText(lastname);
                                        txtPhone.setText(phone);
                                        txtAdress.setText(address);
                                        txtEmail.setText(email);
                                        txtGender.setText(genre);
                                        //account
                                        txtNumber.setText(number);
                                        txtAmount.setText(balance);
                                    }
                                });

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}