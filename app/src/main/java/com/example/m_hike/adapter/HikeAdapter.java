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
import com.example.m_hike.model.Hike;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeHolder> {
    private final RecyclerViewListener listener;
    Context context;
    ArrayList<Hike> hikeList;

    public HikeAdapter(Context context, ArrayList<Hike> hikeList, RecyclerViewListener listener) {
        this.context = context;
        this.hikeList = hikeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hike_item_card, parent, false);
        return new HikeHolder(view);
    }


    public void onBindViewHolder(@NonNull HikeHolder holder, int position) {
        Hike hike = hikeList.get(position);

        holder.tvName.setText(hike.getName());
        holder.tvDate.setText(hike.getDateOfHike());
        holder.tvLocationHike.setText("Location : " + hike.getLocation());
        holder.tvLengthHike.setText(hike.getLengthOfHeight() + " Km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.hikeItemClick(hike.getHikeId());
            }
        });

        holder.ivEditHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editHikeClick(hike.getHikeId());
            }
        });

        holder.ivDeleteHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteHikeClick(hike.getHikeId());
            }
        });

        holder.mbAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addObservation(hike.getHikeId());
            }
        });

        holder.mbObservationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showObservationList(hike.getHikeId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }


    public interface RecyclerViewListener {
        void hikeItemClick(int hikeId);

        void editHikeClick(int hikeId);

        void deleteHikeClick(int hikeId);

        void addObservation(int hikeId);

        void showObservationList(int hikeId);
    }

    public static class HikeHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvLocationHike, tvLengthHike;
        ImageView ivEditHike, ivDeleteHike;

        MaterialButton mbAddObservation, mbObservationList;

        public HikeHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocationHike = itemView.findViewById(R.id.tvLocationHike);
            tvLengthHike = itemView.findViewById(R.id.tvLengthHike);
            ivEditHike = itemView.findViewById(R.id.ivEditHike);
            ivDeleteHike = itemView.findViewById(R.id.ivDeleteHike);
            mbAddObservation = itemView.findViewById(R.id
                    .mbAddObservation);
            mbObservationList = itemView.findViewById(R.id.mbObservationList);

        }
    }

}
