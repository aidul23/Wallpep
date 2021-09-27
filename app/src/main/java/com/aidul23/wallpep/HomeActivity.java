package com.aidul23.wallpep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private RecyclerView staggeredRv;
    private StaggeredRecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager manager;

    FirebaseFirestore db;

    ProfileInfo profileInfo;
    ImageView profile;
    ArrayList<ImageModel> mImages = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        profile = findViewById(R.id.profile_Picture_home_id);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        Uri photoUrl = user.getPhotoUrl();
//        profileInfo = new ProfileInfo(null,null,photoUrl);
//
//        Picasso.get()
//                .load(profileInfo.getImage())
//                .into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        staggeredRv = findViewById(R.id.recyclerViewId);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredRv.setLayoutManager(manager);


        db = FirebaseFirestore.getInstance();

        EventChangeListener();


    }

    private void EventChangeListener() {
        db.collection("all")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                            ImageModel imageModel = new ImageModel(
                                    snapshot.getData().get("imageDescription").toString(),
                                    snapshot.getData().get("imageName").toString(),
                                    snapshot.getData().get("imageUrl").toString(),
                                    Boolean.valueOf(snapshot.getData().get("isLiked").toString())
                            );
                            imageModel.setId(snapshot.getReference().getId());
                            mImages.add(imageModel);
                            Log.d(TAG, "onSuccess: " + imageModel.toString());
                        }


                        adapter = new StaggeredRecyclerViewAdapter(mImages);
                        staggeredRv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(ImageModel imageModel) {
                                Intent intent = new Intent(getApplicationContext(),GalleryActivity.class);
                                intent.putExtra("imageModel",imageModel);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });

    }

}