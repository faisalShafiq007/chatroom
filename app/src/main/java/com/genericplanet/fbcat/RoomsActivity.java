package com.genericplanet.fbcat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.genericplanet.fbcat.adapters.RoomAdapter;
import com.genericplanet.fbcat.classes.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity implements RoomAdapter.RoomListener {

    private Button logoutbutton;
    private ProgressBar progressBar;
    TextView noRooms;
    ImageButton newMessage;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RoomAdapter adapter;
    FirebaseFirestore firestore;
    ArrayList <Room> rooms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        getSupportActionBar().setTitle("Chat Rooms");
        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_room);
        logoutbutton=findViewById(R.id.logoutButton);
        progressBar=findViewById(R.id.progressBarRooms);
        newMessage=findViewById(R.id.newMessageButton);
        noRooms=findViewById(R.id.Norooms);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SearchUser.class);
                startActivity(intent);
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Signout();
            }
        });
        UpdateUI();
    }

    private void UpdateUI() {
        String UserId = mAuth.getCurrentUser().getUid();
        firestore.collection("Rooms").whereArrayContains("participants",UserId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(adapter == null) {
                    adapter = new RoomAdapter(queryDocumentSnapshots.getDocuments());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RoomsActivity.this));
                    adapter.setListener(RoomsActivity.this);
                }
                else
                    adapter.setData(queryDocumentSnapshots.getDocuments());
                progressBar.setVisibility(View.INVISIBLE);
                if(queryDocumentSnapshots.getDocuments()==null)
                    noRooms.setVisibility(View.VISIBLE);
                else
                    noRooms.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void Signout() {
        mAuth.signOut();
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRoomClickListener(String id) {
        Intent intent=new Intent(getApplicationContext(),MessageActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }
}
