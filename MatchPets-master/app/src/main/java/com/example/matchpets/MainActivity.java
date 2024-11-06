package com.example.matchpets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.matchpets.Cards.Cards;
import com.example.matchpets.Cards.arrayAdapter;
import com.example.matchpets.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Cards cardsData[];
    private com.example.matchpets.Cards.arrayAdapter arrayAdapter;
    private int i;

    //this variable store all the info about logged in user
    private FirebaseAuth myAuth;

    private String currentUId;
    private DatabaseReference petsDb;


    ListView listView;
    List<Cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petsDb = FirebaseDatabase.getInstance().getReference().child("Pets");
        myAuth = FirebaseAuth.getInstance();
        currentUId = myAuth.getCurrentUser().getUid();

        checkPetType();

        //add is for name of the card
        rowItems = new ArrayList<Cards>();
        getSupportActionBar().setTitle("Home");

        //here layout is textview for cards' color and text
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);

        //flingListener is for click and move the cards
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                Cards obj = (Cards) dataObject;
                String petId = obj.getUserId();
                petsDb.child(petId).child("connections").child("nope").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Not Intrested", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String petId = obj.getUserId();
                petsDb.child(petId).child("connections").child("yes").child(currentUId).setValue(true);
                isConnectionMatch(petId);
                Toast.makeText(MainActivity.this, "Intrested", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String petId) {
        DatabaseReference currentPetConnectionDb = petsDb.child(currentUId).child("connections").child("yes").child(petId);
        currentPetConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
//                    petsDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    petsDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);

//                    petsDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                    petsDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("chatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String petType;
    private String otherPetType;

    public void checkPetType() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference petDb = petsDb.child(user.getUid());

        petDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        petType = snapshot.child("type").getValue().toString();
                        switch (petType) {
                            case "Dog":
                                otherPetType = "Cat";
                                break;
                            case "Cat":
                                otherPetType = "Dog";
                                break;
                        }
                        getSameTypePets();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        petDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    if(snapshot.exists()){
//                        if(snapshot.child("type").getValue() != null){
//                            petType = Objects.requireNonNull(snapshot.child("type").getValue()).toString();
//                            switch (petType){
//                                case "Dog":
//                                    otherPetType = "Cat";
//                                    break;
//                                case "Cat":
//                                    otherPetType = "Dog";
//                                    break;
//                            }
//                            getSameTypePets();
//                        }
//
//                    }
//                }
//
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

//        DatabaseReference catDb = FirebaseDatabase.getInstance().getReference().child("Pets").child("Cat");
//        catDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.getKey().equals(user.getUid())) {
//                    petType = "Cat";
//                    otherPetType = "Dog";
//                    getSameTypePets();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
    }

    public void getSameTypePets() {
        petsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUId) && !snapshot.child("connections").child("yes").hasChild(currentUId) && snapshot.child("type").getValue().toString().equals(petType)) {
                        String profileImageUrl = "default";
                        if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                        }

                        Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void logoutUser(View view) {
        myAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

//    public void goToSettings(View view) {
//        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//        startActivity(intent);
//        return;
//    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, PetProfile.class);
        startActivity(intent);
        return;
    }
    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
}