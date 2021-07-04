
        package com.example.paybill;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BillFragment extends Fragment {

    private CardView cbPaidBill, cbUnpaidBill,cdAddBill;
    private ListView listBills;
    private Handler handler = new Handler();
    private ListAdapter adapter;
    private ArrayList<HashMap<String,String>> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bill, container, false);
        listBills = view.findViewById(R.id.listBills);
        cbPaidBill = view.findViewById(R.id.cdPaidBill);
        cbUnpaidBill = view.findViewById(R.id.cdUnpaidBill);
        cdAddBill = view.findViewById(R.id.cdAddBill);

        cdAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,new AddBillFragment())
                        .addToBackStack(null).commit();
            }
        });

        cbPaidBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), getString(R.string.error_fields), Toast.LENGTH_SHORT).show();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,new PaidBillFragment())
                        .addToBackStack(null).commit();
            }
        });

        cbUnpaidBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,new UnpaidBillFragment())
                        .addToBackStack(null).commit();
            }
        });

        listBills = view.findViewById(R.id.listBills);
        //on cree le tableau
        arrayList=new ArrayList<>();
        billServer();
        return view;
    }

    public void billServer(){
        String url  = "https://paybill-api.herokuapp.com/bills/user/"+MainActivity.getUser().getId();
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


                try {
                    String myResponse = response.body().string();
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
                        String status = biller.getString("status");
                        System.out.println(i + " numero: " + numero + " deadline : " + deadline
                                + " amount: " + amount + " type : " + type
                                + " status: " + status
                        );

                        HashMap<String, String> data = new HashMap<>();

                        //met les donnes dans le tableau
                        data.put("numero", numero);
                        data.put("deadline", deadline);
                        data.put("amount", amount);
                        data.put("type", type);
                        data.put("status", status);
                        arrayList.add(data);

                        adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.bill_item
                                ,new String[]{"numero", "deadline","amount","type","status"}
                                ,new int[]{R.id.numero, R.id.deadline,R.id.amount,R.id.type,R.id.status});

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listBills.setAdapter(adapter);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}