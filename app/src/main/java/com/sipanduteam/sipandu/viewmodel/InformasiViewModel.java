package com.sipanduteam.sipandu.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sipanduteam.sipandu.model.InformasiResponse;
import com.sipanduteam.sipandu.repository.InformasiBerandaRepository;
import com.sipanduteam.sipandu.repository.InformasiRepository;

public class InformasiViewModel extends ViewModel {
    private MutableLiveData<InformasiResponse> informasiResponseMutableLiveData;
    private InformasiRepository informasiRepository;

    public void init(int flag){
        if (informasiResponseMutableLiveData != null){
            return;
        }
        informasiRepository = informasiRepository.getInstance();
        informasiResponseMutableLiveData = informasiRepository.getInformasi(flag);

    }

    public LiveData<InformasiResponse> getInformasiRepository() {
        return informasiResponseMutableLiveData;
    }
}
