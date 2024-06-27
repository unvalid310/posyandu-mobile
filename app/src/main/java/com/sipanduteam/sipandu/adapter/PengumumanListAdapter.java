package com.sipanduteam.sipandu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sipanduteam.sipandu.R;
import com.sipanduteam.sipandu.activity.posyandu.KegiatanDetailActivity;
import com.sipanduteam.sipandu.activity.posyandu.PengumumanDetailActivity;
import com.sipanduteam.sipandu.model.Informasi;
import com.sipanduteam.sipandu.model.posyandu.Kegiatan;
import com.sipanduteam.sipandu.model.posyandu.Pengumuman;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PengumumanListAdapter extends RecyclerView.Adapter<PengumumanListAdapter.ViewHolder> {
    private Context mContext;
    String pengumumanKey = "PENGUMUMAN_ID";
    private ArrayList<Pengumuman> pengumumanArrayList;
    private int position;
    String informasiKey = "INFORMASI_LINK", informasiTitleKey = "INFORMASI_TITLE";
    String informasiSupporting;
    String duar;

    public PengumumanListAdapter(Context context, ArrayList<Pengumuman> pengumumanArrayList) {
        mContext = context;
        this.pengumumanArrayList = pengumumanArrayList;
    }

    @NonNull
    @Override
    public PengumumanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pengumuman_card_layout, parent, false);
        return new PengumumanListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengumumanListAdapter.ViewHolder holder, int position) {
//        holder.circularProgressIndicator.setVisibility(View.VISIBLE);

        //TODO pastiin url image nya nanti waktu production
        // replaceAll("http://192.168.1.3:1107", "http://sipandu.internationalbusinessmarin.com")
        holder.progressContainer.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(pengumumanArrayList.get(position).getFoto())
                .into(holder.pengumumanImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressContainer.setVisibility(View.GONE);
                        holder.pengumumanImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressContainer.setVisibility(View.GONE);
                        holder.imageFailedLoad.setVisibility(View.VISIBLE);
                    }
                });
        holder.pengumumanTitle.setText(pengumumanArrayList.get(position).getJudulPengumuman());
        holder.pengumumanDate.setText(changeDateFormat(pengumumanArrayList.get(position).getTanggal()));
        duar = Html.fromHtml(pengumumanArrayList.get(position).getPengumuman()).toString();

        if (duar.length() > 80) {
            holder.pengumumanSupportingText.setText(duar.substring(0, 80) + "...");
        } else {
            holder.pengumumanSupportingText.setText(duar.trim());
        }
    }

    @Override
    public int getItemCount() {
        return pengumumanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView pengumumanTitle;
        private final AppCompatTextView pengumumanDate;
        private final ImageView pengumumanImage;
        private final LinearLayout imageFailedLoad;
        private final LinearLayout progressContainer;
        private final AppCompatTextView pengumumanSupportingText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pengumumanTitle = itemView.findViewById(R.id.pengumuman_title);
            pengumumanDate = itemView.findViewById(R.id.pengumuman_date);
            pengumumanImage = itemView.findViewById(R.id.pengumuman_image);
            progressContainer = itemView.findViewById(R.id.pengumuman_image_progress);
            imageFailedLoad = itemView.findViewById(R.id.pengumuman_failed_image_load);
            pengumumanSupportingText = itemView.findViewById(R.id.pengumuman_supporting_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    Pengumuman pengumuman = pengumumanArrayList.get(position);
                    Intent pengumumanDetail = new Intent(mContext, PengumumanDetailActivity.class);
                    Gson gson = new Gson();
                    String pengumumanJson = gson.toJson(pengumuman);
                    pengumumanDetail.putExtra(pengumumanKey, pengumumanJson);
                    mContext.startActivity(pengumumanDetail);
                }
            });
        }
    }

    public String changeDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMMM-yyyy";
        SimpleDateFormat input = new SimpleDateFormat(inputPattern);
        SimpleDateFormat output = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = input.parse(time);
            str = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
