package com.example.auth_v1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProfilActivity extends AppCompatActivity {

    private Button logout;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userId;
    private EditText address1,address2;
    private Spinner spinnerCountry,spinnerState,spinnerCity;
    private ImageView gps_btn;
    CountryCodePicker ccp;
    EditText editTextCarrierNumber;
    private TextView email,username,cin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        logout=findViewById(R.id.logout);

       logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfilActivity.this, MainActivity.class));
            }
        });

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId =user.getUid();
        username=findViewById(R.id.complete_username);
        email=findViewById(R.id.complete_email);
        cin= findViewById(R.id.complete_cin);
        spinnerCity =findViewById(R.id.spinnerCity);
        spinnerCountry= findViewById(R.id.spinnerCountry);
        spinnerState = findViewById(R.id.spinnerState);
        address1=findViewById(R.id.address1);
        gps_btn= findViewById(R.id.setLocationGps);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
       ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
       ccp.detectLocaleCountry(false);



        ActivityCompat.requestPermissions(ProfilActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        gps_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                locationEnabled();
                GpsTracker gt = new GpsTracker(getApplicationContext());
                Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());

                Location l = gt.getLocation();

                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    try {
                        List<Address> addressList = geocoder.getFromLocation(
                                lat, lon, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                                System.out.println("adress line" + address.getAddressLine(i));
                            }

                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getAdminArea()).append("\n");
                            sb.append(address.getCountryName());
                            String s = sb.toString();
                            address1.setText(s);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                User userProfil = snapshot.getValue(User.class);
                if(userProfil!=null){
                    String name_val= userProfil.getName();
                    String email_val=userProfil.getEmail();
                    String cin_val = userProfil.getCin();
                    username.setText(name_val);
                    email.setText(email_val);
                    cin.setText(cin_val);
                }
            }
            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(ProfilActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> test =  geocoder.getFromLocationName(Locale.getDefault().getCountry(),100);
            for (Address  a: test
                 ) {
                System.out.println(a.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();



            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
       /* for (String country : countries) {
            System.out.println(country);
        }*/

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        // Apply the adapter to the your spinner
        spinnerCountry.setAdapter(countryAdapter);

    }
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(ProfilActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }





}