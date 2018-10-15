package com.genericplanet.fbcat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.genericplanet.fbcat.R;
import com.genericplanet.fbcat.classes.Users;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

//    ArrayList<Users> users;
    Users users;
    CustomItemClickListener listener;

    public UserAdapter(CustomItemClickListener listener){
        this.listener=listener;
        ArrayList<Users>users=new ArrayList<>();
    }
    public void setData(Users users) {
        this.users = users;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_holder, parent, false);
        UserAdapter.UserViewHolder holder=new UserAdapter.UserViewHolder(v);
        final UserViewHolder viewHolder=new UserViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,viewHolder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        Users user=users.get(position);

        holder.userName.setText(users.getusername());
        holder.fullName.setText(users.getname());




    }

    @Override
    public int getItemCount() {
//        return users.size();
        return 1;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView fullName,userName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName=itemView.findViewById(R.id.Name);
            userName=itemView.findViewById(R.id.userName);

        }
    }
    public interface CustomItemClickListener {

        public void onItemClick(View v, int position);

    }
}
