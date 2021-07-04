
        package com.example.paybill;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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


public class PaidBillFragment extends Fragment {

    private String myResponse;
    private ListView listPaidBills;
    private ArrayList<HashMap<String,String>> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_paid_bill, container, false);
        listPaidBills = view.findViewById(R.id.listUnpaidBills);

        //on cree le tableau
        arrayList=new ArrayList<>();
        billPaidServer();
        return view;
    }

    private void billPaidServer() {
        String url  = "https://paybill-api.herokuapp.com/bills/paid/user/"+MainActivity.getUser().getId();
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
                                    System.out.println(i + " numero: " + numero + " deadline : " + deadline
                                            + " amount: " + amount + " type : " + type
                                    );

                                    HashMap<String, String> data = new HashMap<>();

                                    //met les donnes dans le tableau
                                    data.put("numero", numero);
                                    data.put("deadline", deadline);
                                    data.put("amount", amount);
                                    data.put("type", type);
                                    arrayList.add(data);


                                    ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.bill_paid_item
                                            ,new String[]{"numero", "deadline","amount","type"}
                                            ,new int[]{R.id.numero, R.id.deadline,R.id.amount,R.id.type});
                                    listPaidBills.setAdapter(adapter);
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
}