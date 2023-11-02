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


public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationHolder> {
    private final RecyclerViewListener listener;
    Context context;
    ArrayList<Observation> observationList;

    public ObservationAdapter(Context context, ArrayList<Observation> observationList, RecyclerViewListener listener) {
        this.context = context;
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_list_item, parent, false);
        return new ObservationHolder(view);
    }


    public void onBindViewHolder(@NonNull ObservationHolder holder, int position) {
        Observation observation = observationList.get(position);

        holder.tvAnimals.setText(observation.getAnimals());
        holder.tvVegatation.setText(observation.getVegetation());
        holder.tvDateTime.setText(observation.getDate() + " at " + observation.getTime());

        holder.ivEditObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editObservationClick(observation.get_id());
            }
        });

        holder.ivDeleteObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteObservationClick(observation.get_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }


    public interface RecyclerViewListener {
        void editObservationClick(int observation_id);

        void deleteObservationClick(int observation_id);

    }

    public static class ObservationHolder extends RecyclerView.ViewHolder {

        TextView tvAnimals, tvVegatation, tvDateTime;
        ImageView ivEditObservation, ivDeleteObservation;

        public ObservationHolder(View itemView) {
            super(itemView);

            tvAnimals = itemView.findViewById(R.id.tvAnimals);
            tvVegatation = itemView.findViewById(R.id.tvVagetation);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            ivEditObservation = itemView.findViewById(R.id.ivEditObservation);
            ivDeleteObservation = itemView.findViewById(R.id.ivDeleteObservation);

        }
    }

}
