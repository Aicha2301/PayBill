
        package com.example.paybill;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UnpaidBillFragment extends Fragment {

    private String myResponse;
    private ListView listUnpaidBills;
    private String unpaidBill;
    private ArrayList<HashMap<String,String>> arrayList;


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_unpaid_bill, container, false);
        listUnpaidBills = view.findViewById(R.id.listUnpaidBills);

        //on cree le tableau
        arrayList=new ArrayList<>();
        billUnpaidServer();

        //retrouve le bouton;

        listUnpaidBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //recuperer l'element cliquer
                //String numero =  arrayList.get(position).get("numero");
                //Toast.makeText(getActivity(), numero, Toast.LENGTH_SHORT).show();
                String detail = arrayList.get(position).get("description");
                int ident = Integer.parseInt(arrayList.get(position).get("numero"));
                //afficher les details
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Facture N°" +ident);
                dialog.setMessage(detail);
                dialog.setNegativeButton( getString(R.string.back), null);
                dialog.setPositiveButton("Payer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //on va payer la facture
                        payBill(ident,MainActivity.getUser().getId());
                        Toast.makeText(getActivity(), "payer", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    public void billUnpaidServer(){
        String url  = "https://paybill-api.herokuapp.com/bills/unpaid/user/"+ MainActivity.getUser().getId();
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
                myResponse = response.body().string();
                if(response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject reader = new JSONObject(myResponse);
                                JSONArray bills = reader.getJSONArray("bills"); // get the whole json array list
                                System.out.println("json size is : " + bills.length());

                                //parcours le tableau des billers
                                for (int i = 0; i < bills.length(); i++) {
                                    JSONObject biller = bills.getJSONObject(i);
                                    String numero = biller.getString("bill_id");
                                    String deadline = biller.getString("deadline");
                                    String amount = biller.getString("amount");
                                    String type = biller.getString("type");
                                    String description = biller.getString("description");
                                    String image = biller.getString("image");
                                    System.out.println(i + " numero: " + numero + " deadline : " + deadline
                                            + " amount: " + amount + " type : "
                                            + type+ " description : " +
                                            " image : " + image
                                    );

                                    HashMap<String, String> data = new HashMap<>();

                                    //met les donnes dans le tableau
                                    data.put("numero", numero);
                                    data.put("deadline", deadline);
                                    data.put("amount", amount);
                                    data.put("type", type);
                                    data.put("description", description);
                                    data.put("image", image);
                                    arrayList.add(data);

                                    ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.bill_item
                                            ,new String[]{"numero", "deadline","amount","type","status"}
                                            ,new int[]{R.id.numero, R.id.deadline,R.id.amount,R.id.type});
                                    listUnpaidBills.setAdapter(adapter);
                                }

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void payBill(int ident, int id) {
        System.out.println("biller_id=" + ident + "id" + id);
        //Toast.makeText(getActivity(), "Facture numero"+id + "payer par"+ident, Toast.LENGTH_SHORT).show();
        String url = "https://paybill-api.herokuapp.com/bills/pay";
        OkHttpClient client = new OkHttpClient();
        //creation de la requette
        //Request request = new Request()
        //Create a JSONObject with the data to be sent to the server
        try {
            JSONObject jo = new JSONObject()
                    .put("user_id", id)
                    .put("biller_id", ident);

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
                            //responseTxt.setText(message);
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.body().string());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}