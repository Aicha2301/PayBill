package com.example.paybill;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillerFragment extends Fragment {

    //tableau des facturieux
    private String myResponse;
    private ListView listBillers;
    private ArrayList<HashMap<String,String>> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_biller, container, false);
        listBillers = view.findViewById(R.id.listBiller);
        //on cree le tableau
        arrayList=new ArrayList<>();
        billerServer();
        return view;
    }

    public void billerServer(){
        String url  = "https://paybill-api.herokuapp.com/billers";
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
                                JSONArray billers = reader.getJSONArray("billers"); // get the whole json array list
                                System.out.println("json size is : " + billers.length());

                                //parcours le tableau des billers
                                for (int i = 0; i < billers.length(); i++) {
                                    JSONObject biller = billers.getJSONObject(i);
                                    String name = biller.getString("name");
                                    String phone = biller.getString("phone");
                                    System.out.println(i + " Name: " + name + " Phone : " + phone);

                                    HashMap<String, String> data = new HashMap<>();

                                    //met les donnes dans le tableau
                                    data.put("name", name);
                                    data.put("phone", phone);
                                    arrayList.add(data);

                                    ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.biller_item
                                            , new String[]{"name", "phone"}, new int[]{R.id.name, R.id.power});
                                    listBillers.setAdapter(adapter);
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