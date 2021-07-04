
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sonatel = new LatLng(14.711534, -17.470874);
        mMap.addMarker(new MarkerOptions().position(sonatel).title("Sonatel").snippet("Tel: +221338391200, Site:  sonatel.sn, Adresse: Cité Keur Gorgui, Voie de degagement N, Dakar"));

        LatLng canalplus = new LatLng(14.7200982, -17.4341176);
        mMap.addMarker(new MarkerOptions().position(canalplus).title("Canal-plus").snippet("Tel: +221338895040, Site: canalplus-afrique.com, Adresse: Avenue Hassan, II, Senegal"));

        LatLng sde = new LatLng(14.7000177, -17.4532227);
        mMap.addMarker(new MarkerOptions().position(sde).title("SDE").snippet("Tel: +221338690300, Site: sde.sn, Adresse: Centre de Hann, Route du Front de Terre"));

        LatLng aquatech = new LatLng(14.7672985, -16.9481079);
        mMap.addMarker(new MarkerOptions().position(aquatech).title("Aquatech").snippet("Tel: +221338690300, Site: aquatech.sn, Adresse: Thiès, Sénégal"));

        LatLng axa = new LatLng(14.6689088, -17.435977);
        mMap.addMarker(new MarkerOptions().position(axa).title("Axa Assurance").snippet("Tel: +221784946215, Site: axa.sn, Adresse: 5, Place de l’Indépendance BP 182 Dakar, SENEGAL"));

        LatLng senelec = new LatLng(14.6734416, -17.4436307);
        mMap.addMarker(new MarkerOptions().position(senelec).title("Senelec").snippet("Tel: +221784946215, Site: senelec.sn, Adresse: 28 Rue Vincens, Dakar, Sénégal"));

        LatLng eiffage = new LatLng(14.6919142, -17.4353772);
        mMap.addMarker(new MarkerOptions().position(eiffage).title("Eiffage").snippet("Tel: +221784946215, Site: eiffage.sn, Adresse: Avenue Félix Éboué x, Route des Brasseries, Dakar, Sénégal"));


        //afficher  la position de esmt en zoom par defaut
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonatel, 12));
        //pout le carre  pour voir la vue satellite
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // pour pouvoir appeller directement
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Sonatel"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Sonatel team , I want more information about payements services");
                    startActivity(intent);


                } else if (marker.getTitle().equals("Canal-plus"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Canal-plus team , I want more information about payements services");
                    startActivity(intent);

                } else if (marker.getTitle().equals("SDE"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello SDE team , I want more information about payements services");
                    startActivity(intent);

                } else if (marker.getTitle().equals("Axa Assurance"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Axa Assurance team , I want more information about payements services");
                    startActivity(intent);

                } else if (marker.getTitle().equals("Senelec"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Senelec team , I want more information about payements services");
                    startActivity(intent);

                }
                else if (marker.getTitle().equals("Eiffage"))
                {
                    //action_SENTO envoye d'envoyer un sms en mm la personne
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:776585821"));
                    intent.putExtra("sms_body","hello Eiffage team , I want more information about payements services");
                    startActivity(intent);

                }
                return false;
            }
        });
    }
}