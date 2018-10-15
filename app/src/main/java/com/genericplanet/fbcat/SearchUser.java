package com.genericplanet.fbcat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.genericplanet.fbcat.adapters.UserAdapter;
import com.genericplanet.fbcat.classes.Room;
import com.genericplanet.fbcat.classes.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUser extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    UserAdapter adapter;
    EditText searchEdittext,descEditText,titleEditText;
    ImageButton searchButton;
    CollectionReference newRoom;
    Users user=new Users();
    ArrayList<Users> users=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchButton=findViewById(R.id.searchButton);
        searchEdittext=findViewById(R.id.serachEditText);
        descEditText=findViewById(R.id.descEditText);
        titleEditText=findViewById(R.id.titleEditText);
        getSupportActionBar().setTitle("Search User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newRoom=firestore.collection("Rooms");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String username= searchEdittext.getText().toString();

               firestore.collection("users").whereEqualTo("username",username.toLowerCase())
                       .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                       for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                           user = snapshot.toObject(Users.class);
//                           users.add(user);

                       }
                       adapter=new UserAdapter(new UserAdapter.CustomItemClickListener() {
                           @Override
                           public void onItemClick(View v, int position) {
                               if(TextUtils.isEmpty(descEditText.getText())||
                                       TextUtils.isEmpty(titleEditText.getText())
                                       ||TextUtils.isEmpty(searchEdittext.getText())){
                                    if(TextUtils.isEmpty(descEditText.getText()))
                                   descEditText.setError("Enter Description Please");
                                   if(TextUtils.isEmpty(titleEditText.getText()))
                                   titleEditText.setError("Enter Title Please");
                                   if(TextUtils.isEmpty(searchEdittext.getText()))
                                       searchEdittext.setError("Enter Description Please");
                               }
                               else{
//                                   String UID=users.get(0).getid();
                                    String UID=user.getid();
                                   ArrayList<String> participants =new ArrayList();
                                   participants.add(UID);
                                   participants.add(mAuth.getCurrentUser().getUid());
                                   Room room=new Room(participants,descEditText.getText().toString(),
                                           titleEditText.getText().toString());
                                   newRoom.add(room);

                                   Intent intent=new Intent(getApplicationContext(),RoomsActivity.class);
                                   startActivity(intent);
                                   finish();
                               }

                           }
                       });
                       recyclerView.setAdapter(adapter);
//                       adapter.setData(users);
                       adapter.setData(user);
                       adapter.notifyDataSetChanged();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(Exception e) {

                   }
               });
            }
        });


    }
}
