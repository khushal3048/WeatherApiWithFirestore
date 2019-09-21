package com.example.weatherapiwithfirestore;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Recyadapter extends RecyclerView.Adapter<Recyadapter.ViewHolder> {

    private ArrayList<ConsolidatedWeather> weatherArray;
    private Context c;

    public Recyadapter(ArrayList<ConsolidatedWeather> weatherArray, Context c) {
        this.weatherArray = weatherArray;
        this.c = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imageUrl = imageString(weatherArray.get(position).getWeatherStateAbbr());
        setupImage(imageUrl,holder.imgv);
        holder.txt_weatherName.setText(weatherArray.get(position).getWeatherStateName());
        try {
            holder.txt_day.setText(getDay(weatherArray.get(position).getApplicableDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return weatherArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView imgv;
        TextView txt_weatherName,txt_day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgv = itemView.findViewById(R.id.img_weatherDay);
            txt_weatherName = itemView.findViewById(R.id.txt_weatherName);
            txt_day = itemView.findViewById(R.id.txt_day);

            itemView.setTag(this);

        }
    }

    public String imageString(String abbr){

        return "https://www.metaweather.com/static/img/weather/png/" + abbr + ".png";

    }

    public void setupImage(String url, ImageView imgV){

        Glide.with(c).asBitmap().load(url).into(imgV);

    }

    public String getDay(String d) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(d);
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date);

        return dayOfTheWeek;
    }
}
