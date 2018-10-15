package com.genericplanet.fbcat.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genericplanet.fbcat.R;
import com.genericplanet.fbcat.classes.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    ArrayList<Message> messages;
    Listener listener;
    public  MessageAdapter(){
        messages = new ArrayList<>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void addMessage(Message message){
        if(message != null){
            messages.add(message);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_message, parent, false);
        MessageHolder holder = new MessageHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        final Message message = messages.get(position);
         if (message.getImg()==null||message.getImg()==""){
         holder.contentView.setText(message.getContent());
         holder.timeView.setText(message.getFormmattedTime());
        }

        if(message.getImg() != null&&message.getImg()!=""){
            if(message.getContent().isEmpty()||message.getContent()==" ")
            {
               holder.contentView.setVisibility(View.GONE);
            }
            holder.contentView.setText(message.getContent());
            holder.timeView.setText(message.getFormmattedTime());
            holder.img.setVisibility(View.VISIBLE);
            Picasso.get().load(message.getImg()).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                       listener.onImageClickListener(message.getImg());
                    }
                }
            });
        }

        if(message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#dcf8c6"));
            holder.contentView.setTextColor(Color.parseColor("#000000"));
            holder.timeView.setTextColor(Color.parseColor("#000000"));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.cardView.getLayoutParams();
            params.gravity = Gravity.RIGHT ;
            holder.cardView.setLayoutParams(params);

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public void setList(ArrayList<Message> list) {
        this.messages = list;
    }
    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView contentView;
        TextView timeView;
        ConstraintLayout layout;
        CardView cardView;
        ImageView img;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            contentView = itemView.findViewById(R.id.holder_msg_content);
            timeView = itemView.findViewById(R.id.holder_msg_time);
            layout = itemView.findViewById(R.id.holder_msg_layout);
            cardView = itemView.findViewById(R.id.holder_msg_card);
            img = itemView.findViewById(R.id.holder_message_img);
        }
    }
    public interface Listener
    {
        public void onImageClickListener(String URL);
    }
}
