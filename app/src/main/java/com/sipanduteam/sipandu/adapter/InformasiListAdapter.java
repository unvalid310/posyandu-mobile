package com.sipanduteam.sipandu.adapter;

import android.content.Context;
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

import com.sipanduteam.sipandu.R;
import com.sipanduteam.sipandu.model.Informasi;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InformasiListAdapter extends RecyclerView.Adapter<InformasiListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Informasi> informasiArrayList;
    private int position;
    String informasiKey = "INFORMASI_LINK", informasiTitleKey = "INFORMASI_TITLE";
    String informasiSupporting;
    String duar;

    public InformasiListAdapter(Context context, ArrayList<Informasi> informasiArrayList) {
        mContext = context;
        this.informasiArrayList = informasiArrayList;
    }

    @NonNull
    @Override
    public InformasiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.informasi_card_layout_simpler, parent, false);
        return new InformasiListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformasiListAdapter.ViewHolder holder, int position) {
//        holder.circularProgressIndicator.setVisibility(View.VISIBLE);
        //TODO pastiin link untuk gambarnya sebelum production
        holder.progressContainer.setVisibility(View.VISIBLE);
                Picasso.get()
                .load("http://sipandu.internationalbusinessmarin.com/api/mobileuser/get-informasi-img/" + informasiArrayList.get(position).getId())
                .into(holder.informasiImage, new Callback() {
                    @Override
                    public void onSuccess() {
//                        holder.circularProgressIndicator.setVisibility(View.GONE);
                        holder.progressContainer.setVisibility(View.GONE);
                        holder.informasiImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressContainer.setVisibility(View.GONE);
//                        holder.circularProgressIndicator.setVisibility(View.GONE);
                        holder.imageFailedLoad.setVisibility(View.VISIBLE);
                    }
                });
//        Picasso.get()
//                .load(informasiArrayList.get(position).getFoto().replaceAll("http://192.168.1.3:8000", "http://sipandu.internationalbusinessmarin.com"))
//                .into(holder.informasiImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
////                        holder.circularProgressIndicator.setVisibility(View.GONE);
//                        holder.progressContainer.setVisibility(View.GONE);
//                        holder.informasiImage.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        holder.progressContainer.setVisibility(View.GONE);
////                        holder.circularProgressIndicator.setVisibility(View.GONE);
//                        holder.imageFailedLoad.setVisibility(View.VISIBLE);
//                    }
//                });
        holder.informasiTitle.setText(informasiArrayList.get(position).getJudulInformasi());
        holder.informasiDate.setText(changeDateFormat(informasiArrayList.get(position).getTanggal()));
        holder.informasiViewCount.setText((informasiArrayList.get(position).getDilihat().toString()));

        duar = Html.fromHtml(informasiArrayList.get(position).getInformasi()).toString();

        if (duar.length() > 80) {
            holder.informasiSupportingText.setText(duar.substring(0, 80) + "...");
        } else {
            holder.informasiSupportingText.setText(duar.trim());
        }
    }

    @Override
    public int getItemCount() {
        return informasiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView informasiTitle;
        private final AppCompatTextView informasiDate;
        private final ImageView informasiImage;
//        private final CircularProgressIndicator circularProgressIndicator;
        private final LinearLayout imageFailedLoad;
        private final AppCompatTextView informasiViewCount;
        private final LinearLayout progressContainer;
        private final AppCompatTextView informasiSupportingText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            informasiTitle = itemView.findViewById(R.id.informasi_title);
            informasiDate = itemView.findViewById(R.id.informasi_date);
            informasiImage = itemView.findViewById(R.id.blog_image);
//            circularProgressIndicator = itemView.findViewById(R.id.informasi_image_progress);
            progressContainer = itemView.findViewById(R.id.informasi_image_progress);
            imageFailedLoad = itemView.findViewById(R.id.informasi_failed_image_load);
            informasiViewCount = itemView.findViewById(R.id.informasi_view_count);
            informasiSupportingText = itemView.findViewById(R.id.informasi_supporting_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    Informasi informasi = informasiArrayList.get(position);

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(mContext.getResources().getColor(R.color.primaryColor));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(mContext, Uri.parse("http://sipandu.internationalbusinessmarin.com/blog/detail/" + informasi.getSlug()));
//
//                    Intent informasiShow = new Intent(mContext, InformasiShowActivity.class);
//                    informasiShow.putExtra(informasiKey, informasi.getSlug());
//                    informasiShow.putExtra(informasiTitleKey, informasi.getJudulInformasi());
//                    mContext.startActivity(informasiShow);
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
