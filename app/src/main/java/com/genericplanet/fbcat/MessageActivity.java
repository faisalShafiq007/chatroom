package com.genericplanet.fbcat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.genericplanet.fbcat.adapters.MessageAdapter;
import com.genericplanet.fbcat.classes.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class MessageActivity extends AppCompatActivity implements MessageAdapter.Listener {

    MessageAdapter adapter;
    RecyclerView recyclerView;
    EditText messageEdit;
    ImageButton sendBtn, attachBtn;
    String id;
    CollectionReference messagesCollection;
    ProgressBar messageProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(id==null)
        id = getIntent().getStringExtra("id");
        recyclerView = findViewById(R.id.messages_recycler);
        messageEdit = findViewById(R.id.messages_edittext);
        sendBtn = findViewById(R.id.messages_send);
        attachBtn = findViewById(R.id.messages_attach);
        messageProgress=findViewById(R.id.progressBarMessage);
        adapter = new MessageAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(adapter.getItemCount()-1);

        messagesCollection = FirebaseFirestore.getInstance().collection("Rooms").document(id).collection("messages");

        messagesCollection.orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {



                ArrayList<Message> list = new ArrayList<>();
                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                    Message message = snapshot.toObject(Message.class);
                    list.add(message);

                }
                if(list.size()>1)
               recyclerView.smoothScrollToPosition(list.size()-1);
                adapter.setList(list);
                adapter.notifyDataSetChanged();

            }
        });

                sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String content = messageEdit.getText().toString();
                if(content.isEmpty()!=true&&content!=" ") {
                    Date date = new Date();
                    sendMessage(content, sender, date);
                    messageEdit.setText("");
                }
            }
        });
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageProgress.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent.createChooser(intent, "Pick image");
                startActivityForResult(intent, 71);
            }
        });
    }
    public void sendMessage(String content, String sender, Date date){
        Message message = new Message(content, sender, date);
        message.setImg("");
        messagesCollection.add(message);
    }
    public void sendMessage(Message message){
        messagesCollection.add(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 71){
            if(resultCode == RESULT_OK){
                if(data != null){
                    final Uri uri = data.getData();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference ref = storage.getReference().child(messagesCollection.getId()).child(uri.getLastPathSegment());
                    UploadTask uploadTask = ref.putFile(uri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            messageProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(MessageActivity.this, "Image Added", Toast.LENGTH_LONG).show();
                            messageEdit.setText("");
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override

                                public void onSuccess(Uri uri) {
                                    Message message = new Message(messageEdit.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), new Date());
                                    message.setImg(uri.toString());
                                    sendMessage(message);

                                }

                            }).addOnFailureListener(new OnFailureListener() {

                                @Override

                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(MessageActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MessageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onImageClickListener(String URL) {
        Intent intent=new Intent(getApplicationContext(),fullImage.class);
        intent.putExtra("URL",URL);
        startActivity(intent);
    }
}
