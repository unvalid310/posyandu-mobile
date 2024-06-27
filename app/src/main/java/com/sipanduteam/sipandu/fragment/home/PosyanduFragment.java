package com.sipanduteam.sipandu.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sipanduteam.sipandu.R;
import com.sipanduteam.sipandu.activity.posyandu.KonsultasiTelegramActivity;
import com.sipanduteam.sipandu.activity.posyandu.PosyanduKegiatanActivity;
import com.sipanduteam.sipandu.activity.posyandu.PosyanduMapActivity;
import com.sipanduteam.sipandu.viewmodel.PosyanduViewModel;

import static android.view.View.GONE;

public class PosyanduFragment extends Fragment {

    SharedPreferences userPreferences;
    TextView namaPosyandu, alamatPosyandu;

    Button posyanduCallButton;

    MaterialCardView  openPosyanduScheduleButton, openPosyanduLocationButton, openPosyanduMapCard, addPosyanduBot;

    String email, lat, lon;
    int role;
    Intent posyanduCall = new Intent(Intent.ACTION_DIAL), openPosyanduLoc;


    LinearLayout loadingContainer, failedContainer;
    ScrollView posyanduContainer;
    Button refreshProfile;
    View v;
    PosyanduViewModel posyanduViewModel;

    public PosyanduFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posyanduViewModel = ViewModelProviders.of(getActivity()).get(PosyanduViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_posyandu, container, false);

        userPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        email = userPreferences.getString("email", "empty");
        role = userPreferences.getInt("role", 4);

        posyanduCallButton = v.findViewById(R.id.call_posyandu_button);
        openPosyanduScheduleButton = v.findViewById(R.id.jadwal_posyandu_button);
        openPosyanduLocationButton = v.findViewById(R.id.posyandu_location_button);
        openPosyanduMapCard = v.findViewById(R.id.open_posyandu_map_card);
        addPosyanduBot = v.findViewById(R.id.posyandu_telegram_bot_button);

        refreshProfile = v.findViewById(R.id.posyandu_refresh);
        refreshProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posyanduViewModel.getData(email, role);
                getData();
            }
        });
        loadingContainer = v.findViewById(R.id.posyandu_loading_container);
        posyanduContainer = v.findViewById(R.id.posyandu_layout_container);
        failedContainer = v.findViewById(R.id.posyandu_failed_container);

        namaPosyandu = v.findViewById(R.id.posyandu_name_text);
        alamatPosyandu = v.findViewById(R.id.posyandu_alamat_text);

        openPosyanduMapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPosyanduMap = new Intent(getActivity(), PosyanduMapActivity.class);
                startActivity(openPosyanduMap);
            }
        });

        openPosyanduScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPosyanduSchedule = new Intent(getActivity(), PosyanduKegiatanActivity.class);
                startActivity(openPosyanduSchedule);
            }
        });

        openPosyanduLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://maps.google.com/?q="+lat+","+lon;
                openPosyanduLoc = new Intent(Intent.ACTION_VIEW);
                openPosyanduLoc.setData(Uri.parse(url));
                startActivity(openPosyanduLoc);
            }
        });

        posyanduCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(posyanduCall);
            }
        });
        getData();
        return v;
    }

    public void getData() {
        setLoadingContainerVisible();
        posyanduViewModel.init(email, role);
        posyanduViewModel.getUserPosyandu().observe(getViewLifecycleOwner(), posyanduUserResponse -> {
            if (posyanduUserResponse != null) {
                namaPosyandu.setText(posyanduUserResponse.getPosyandu().getNamaPosyandu());
                alamatPosyandu.setText(posyanduUserResponse.getPosyandu().getAlamat());
                posyanduCall.setData(Uri.parse("tel:" + posyanduUserResponse.getPosyandu().getNomorTelepon()));
                posyanduCallButton.setText("Hubungi " + namaPosyandu.getText().toString());
                lat = posyanduUserResponse.getPosyandu().getLatitude().toString();
                lon = posyanduUserResponse.getPosyandu().getLongitude().toString();
                setPosyanduContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        posyanduContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        posyanduContainer.setVisibility(GONE);
    }

    public void setPosyanduContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        posyanduContainer.setVisibility(View.VISIBLE);
    }
}