package com.example.paybill;

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


public class AddBillFragment extends Fragment {
 private Spinner spTypeBill ;
 private EditText txtDescription , txtAmount , txtDeadline ;
 private Button btnSave ;
 private String deadLine , amount , description , type ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_bill, container, false);
        spTypeBill = view.findViewById(R.id.tab_type_bills);
        txtDescription = view.findViewById(R.id.descriptionBill);
        txtAmount = view.findViewById(R.id.amount);
        txtDeadline = view.findViewById(R.id.deadline);
        btnSave = view.findViewById(R.id.btnSaveBill);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tab_type_bills, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spTypeBill.setAdapter(adapter);
        //ajout de l'événement sur le button select
        spTypeBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Inflate the layout for this fragment
        return view ;

    }


}