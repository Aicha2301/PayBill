package com.example.paybill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListPaidFragment extends Fragment {
    private ListView listFacturePaye ;
    private String facturePaye , details;
    private String [] tabsFacturePaye , tabDetails ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_paid, container, false);
        //match to element in interface
        listFacturePaye = view.findViewById(R.id.listPaidBill);
        //tableau des formations
        tabsFacturePaye = getResources().getStringArray(R.array.tab_bills_Paid);
        //tableau des details
        tabDetails = getResources().getStringArray(R.array.tab_details);

        //creer un adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,tabsFacturePaye);
        //chargement des données sur la liste
        listFacturePaye.setAdapter(adapter);

        listFacturePaye.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //recuperer la position du
                facturePaye = tabsFacturePaye[i];
                details = tabDetails[i];
                //afficher les details
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle(facturePaye);
                dialog.setMessage(details);
                dialog.setNegativeButton( getString(R.string.back), null);

                dialog.show();

            }
        });

        return  view ;
}
}