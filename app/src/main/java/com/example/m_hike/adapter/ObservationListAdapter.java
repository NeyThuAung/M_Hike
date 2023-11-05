package com.example.m_hike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;
import com.example.m_hike.model.Observation;

import java.util.ArrayList;


public class ObservationListAdapter extends RecyclerView.Adapter<ObservationListAdapter.ObservationListHolder> {
    Context context;
    ArrayList<Observation> observationList;

    public ObservationListAdapter(Context context, ArrayList<Observation> observationList) {
        this.context = context;
        this.observationList = observationList;
    }

    @NonNull
    @Override
    public ObservationListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_detail_item_card, parent, false);
        return new ObservationListHolder(view);
    }


    public void onBindViewHolder(@NonNull ObservationListHolder holder, int position) {
        Observation observation = observationList.get(position);

        holder.tvObservationCount.setText("Observation " + (holder.getAdapterPosition() + 1));

        holder.tvDetailsAnimals.setText(observation.getAnimals());
        holder.tvDetailsVegatation.setText(observation.getVegetation());
        holder.tvDetailsWeather.setText(observation.getWeather());
        holder.tvDetailsTrails.setText(observation.getTrails());
        holder.tvDetailsObservationDate.setText(observation.getDate());
        holder.tvDetailsObservationTime.setText(observation.getTime());

        if (observation.getComments().isEmpty()) {
            holder.tvDetailsComments.setText("-");
        } else
            holder.tvDetailsComments.setText(observation.getComments());


    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public static class ObservationListHolder extends RecyclerView.ViewHolder {

        TextView tvObservationCount, tvDetailsAnimals, tvDetailsVegatation, tvDetailsWeather, tvDetailsTrails, tvDetailsObservationDate, tvDetailsObservationTime, tvDetailsComments;

        public ObservationListHolder(View itemView) {
            super(itemView);

            tvObservationCount = itemView.findViewById(R.id.tvObservationCount);
            tvDetailsAnimals = itemView.findViewById(R.id.tvDetailsAnimals);
            tvDetailsVegatation = itemView.findViewById(R.id.tvDetailsVegatition);
            tvDetailsWeather = itemView.findViewById(R.id.tvDetailsWeather);
            tvDetailsTrails = itemView.findViewById(R.id.tvDetailsTrails);
            tvDetailsObservationDate = itemView.findViewById(R.id.tvDetailsObservationDate);
            tvDetailsObservationTime = itemView.findViewById(R.id.tvDetailsObservationTime);
            tvDetailsComments = itemView.findViewById(R.id.tvDetailsComment);

        }
    }

}
