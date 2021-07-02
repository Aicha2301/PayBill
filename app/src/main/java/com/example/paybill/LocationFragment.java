package com.example.paybill;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return  view ;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng esmtSN = new LatLng(14.7000177, -17.4532227);
        mMap.addMarker(new MarkerOptions().position(esmtSN).title("ESMT-SENEGAL").snippet("Tel: +221338690300, Site: esmt.sn"));

        LatLng ucad = new LatLng(14.6911552, -17.4728325);
        mMap.addMarker(new MarkerOptions().position(ucad).title("UCAD").snippet("Tel: +221784946215, Site: ucad.sn"));

        //afficher  la position de esmt en zoom par defaut
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(esmtSN, 12));
        //pout le carre  pour voir la vue satellite
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // pour pouvoir appeller directement
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("ESMT-SENEGAL"))
                {
                    //action_Dial pour permettre dappeller la personne
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:776585821"));
                    startActivity(intent);

                } else if (marker.getTitle().equals("UCAD"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Ucad team , are you ready for thr project?");
                    startActivity(intent);

                }
                return false;
            }
        });
    }
}