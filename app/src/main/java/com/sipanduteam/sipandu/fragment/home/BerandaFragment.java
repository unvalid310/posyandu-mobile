package com.sipanduteam.sipandu.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.card.MaterialCardView;
import com.sipanduteam.sipandu.R;
import com.sipanduteam.sipandu.activity.informasi.InformasiActivity;
import com.sipanduteam.sipandu.adapter.InformasiListBerandaAdapter;
import com.sipanduteam.sipandu.adapter.PengumumanListAdapter;
import com.sipanduteam.sipandu.model.Informasi;
import com.sipanduteam.sipandu.model.posyandu.Pengumuman;
import com.sipanduteam.sipandu.viewmodel.InformasiBerandaViewModel;
import com.sipanduteam.sipandu.viewmodel.PengumumanViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static java.lang.Integer.valueOf;


public class BerandaFragment extends Fragment {

    LinearLayout loadingContainer, failedContainer, emptyContainer;
    ScrollView homeContainer;
    Button refreshHome;

    private ArrayList<Pengumuman> pengumumanArrayList;

    private InformasiListBerandaAdapter informasiListBerandaAdapter;
    private PengumumanListAdapter pengumumanListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView pengumumanRecycler;
    private LinearLayoutManager pengumumanLayoutManager;

    private ImageView failedImage;
    private AnimatedVectorDrawable failedAnim;
    Drawable d;
    View v;
    PengumumanViewModel pengumumanViewModel;

    SharedPreferences userPreferences;

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengumumanArrayList = new ArrayList<>();
        pengumumanViewModel = ViewModelProviders.of(getActivity()).get(PengumumanViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_beranda, container, false);

            userPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            String email = userPreferences.getString("email", "empty");
            int role = userPreferences.getInt("role", 4);

            refreshHome = v.findViewById(R.id.home_refresh);
            refreshHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pengumumanViewModel.getData(email, role);
                    getData();
                }
            });
            loadingContainer = v.findViewById(R.id.home_loading_container);
            homeContainer = v.findViewById(R.id.home_layout_container);
            failedContainer = v.findViewById(R.id.home_failed_container);
            emptyContainer = v.findViewById(R.id.pengumuman_empty_container);

            failedImage = v.findViewById(R.id.home_failed_icon);
            d = failedImage.getDrawable();

            pengumumanRecycler = v.findViewById(R.id.home_pengumuman_list);
            pengumumanListAdapter = new PengumumanListAdapter(getContext(), pengumumanArrayList);
            pengumumanLayoutManager = new LinearLayoutManager(getContext());
            pengumumanRecycler.setLayoutManager(pengumumanLayoutManager);
            getData();
        }
        return v;
    }

    private void getData() {
        setLoadingContainerVisible();
        pengumumanViewModel.getPengumuman().observe(getViewLifecycleOwner(), pengumumanResponse -> {
            if (pengumumanResponse != null) {
                List<Pengumuman> pengumumanList = pengumumanResponse.getPengumuman();
                pengumumanRecycler.setAdapter(pengumumanListAdapter);
                Log.d("list pengumuman", String.valueOf(pengumumanList.size()));
                pengumumanArrayList.addAll(pengumumanList);
                pengumumanListAdapter.notifyDataSetChanged();
                if (pengumumanList.size() == 0) {
                    emptyContainer.setVisibility(View.VISIBLE);
                    pengumumanRecycler.setVisibility(View.INVISIBLE);
                }
                setHomeContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        homeContainer.setVisibility(GONE);
        if (d instanceof AnimatedVectorDrawable) {
            failedAnim = (AnimatedVectorDrawable) d;
            failedAnim.start();
        }
    }



    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        homeContainer.setVisibility(GONE);
    }

    public void setHomeContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        homeContainer.setVisibility(View.VISIBLE);
    }
}