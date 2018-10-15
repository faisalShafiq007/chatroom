package com.genericplanet.fbcat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.genericplanet.fbcat.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {

    List<DocumentSnapshot> data;
    RoomListener listener;

    public RoomAdapter(List<DocumentSnapshot> data) {
        this.data = data;
    }

    public void setData(List<DocumentSnapshot> data) {
        this.data = data;
    }

    public void setListener(RoomListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_holder, parent, false);

        RoomHolder holder = new RoomHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, final int position) {
        DocumentSnapshot snapshot = data.get(position);

        String title = snapshot.getString("title");
        String desc = snapshot.getString("desc");
        Date timestamp = snapshot.getTimestamp("time").toDate();
        holder.titleView.setText(title);
        holder.descView.setText(desc);
//        TO channge date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String formattedDate  = dateFormat.format(timestamp);
        holder.timeView.setText(formattedDate);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onRoomClickListener(data.get(position).getId());
            }
        });
        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onRoomClickListener(data.get(position).getId());
            }
        });
        holder.descView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onRoomClickListener(data.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, descView, timeView;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.room_image);
            descView = itemView.findViewById(R.id.room_desc);
            timeView = itemView.findViewById(R.id.room_timestamp);
            titleView = itemView.findViewById(R.id.room_title);

        }
    }

    public interface RoomListener
    {
        public void onRoomClickListener(String id);
    }
}
