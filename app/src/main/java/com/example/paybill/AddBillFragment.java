
        package com.example.paybill;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddBillFragment extends Fragment {
    private Spinner spTypeBill ;
    private EditText txtDescription , txtAmount , txtDeadline ;
    private Button btnSave ;
    private String deadline , amount , description , type;
    private List<String> billsList;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_bill, container, false);
        spTypeBill = view.findViewById(R.id.tab_type_bills);
        txtDescription = view.findViewById(R.id.descriptionBill);
        txtAmount = view.findViewById(R.id.amount);
        txtDeadline = view.findViewById(R.id.deadline);
        btnSave = view.findViewById(R.id.btnSaveBill);

        //creation du type de facture
        billsList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, billsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //connecter l'adaptateur
        spTypeBill.setAdapter(adapter);
        //chercher les donnnes
        getBillList();
        //ajout de l'événement sur le button select
        spTypeBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                type = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Inflate the layout for this fragment

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadline = txtDeadline.getText().toString().trim();
                amount = txtAmount.getText().toString().trim();
                description = txtDescription.getText().toString().trim();

                //verifier que les champs sont vides
                if (deadline.isEmpty() || amount.isEmpty() || description.isEmpty() || type.isEmpty()) {
                    String message = getString(R.string.error_fields);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
                    //add bill sur api
                    addBillServer();
                }
            }
        });
        return view ;

    }

    private void getBillList() {
        String url = "https://paybill-api.herokuapp.com/bills/type";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Toast.makeText(getActivity(), mMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject readers = new JSONObject(mMessage);
                            JSONArray typeBillArr = readers.getJSONArray("TypeBill");
                            for(int i =0; i < typeBillArr.length(); i++){
                                JSONObject typeBill = typeBillArr.getJSONObject(i);
                                billsList.add(typeBill.getString("name"));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void clear(){
        txtDescription.setText("");
        txtAmount.setText("");
        txtDeadline.setText("");
    }

    public void addBillServer(){
        String url = "https://paybill-api.herokuapp.com/bills";
        OkHttpClient client = new OkHttpClient();
        //creation de la requette
        //Request request = new Request()
        //Create a JSONObject with the data to be sent to the server

        try {
            JSONObject jo = new JSONObject()
                    .put("deadline", deadline)
                    .put("amount", amount)
                    .put("type", type)
                    .put("description", description)
                    .put("user_id", MainActivity.getUser().getId());
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = getString(R.string.error_connection);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                        if (status.equalsIgnoreCase("Successfully")){
                            clear();
                            //afficher le Toast puis redigerer
                            Toast.makeText(getActivity(),"Facture ajouté avec success", Toast.LENGTH_SHORT).show();
                            //clear field
                        } else {
                            Toast.makeText(getActivity(),"Echec facture", Toast.LENGTH_SHORT).show();
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