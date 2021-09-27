package com.aidul23.wallpep;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;

public class GalleryActivity extends AppCompatActivity implements PopupInflaterListener,
        PopupStateListener, PopupOnHoverListener, View.OnClickListener {

    FloatingActionButton downloadButton, likeButton;
    TextView imageDescription;
    ImageModel imageModel;
    ImageView expandableDownButton, imageView;
    ImageView imagePopup;
    boolean isLiked;
    LinearLayout expandableConstrainLayout, placeLayout;
    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        downloadButton = findViewById(R.id.download_button_id);
        likeButton = findViewById(R.id.like_button_id);
        imageDescription = findViewById(R.id.image_description);
        expandableDownButton = findViewById(R.id.expand_button);
        expandableConstrainLayout = findViewById(R.id.expandable_place_layout);
        placeLayout = findViewById(R.id.place_layout);
        imageView = findViewById(R.id.image);


        Intent intent = getIntent();
        imageModel = intent.getParcelableExtra("imageModel");

        imageDescription.setText(imageModel.getImageDescription());


        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitle(imageModel.getImageName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        getIncomingIntent();

        // Enabling database for resume support even after the application is killed:
//        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                .setDatabaseEnabled(true)
//                .build();

//        PRDownloader.initialize(getApplicationContext(), config);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                checkPermission();
            }
        });

        isLiked = imageModel.isLiked();
        Log.d(TAG, "onCreate: " + isLiked);

        if (isLiked) {
            likeButton.setImageResource(R.drawable.ic_like_fill);
        } else {
            likeButton.setImageResource(R.drawable.ic_like);
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    likeButton.setImageResource(R.drawable.ic_like);
                    isLiked = false;
                } else {
                    isLiked = true;
                    likeButton.setImageResource(R.drawable.ic_like_fill);
                }

                changeLikeOnDatabase(isLiked);
            }
        });


        expandableDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableConstrainLayout.getVisibility() == v.GONE) {
                    TransitionManager.beginDelayedTransition(placeLayout, new AutoTransition());
                    expandableConstrainLayout.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(placeLayout, new AutoTransition());
                    expandableConstrainLayout.setVisibility(View.GONE);
                }
            }
        });

        LongPressPopup popup = new LongPressPopupBuilder(GalleryActivity.this)
                .setTarget(imageView)
                .setPopupView(R.layout.popup_layout, this)
                .setLongPressDuration(750)
                .setDismissOnLongPressStop(true)
                .setDismissOnTouchOutside(true)
                .setDismissOnBackPressed(true)
                .setCancelTouchOnDragOutsideView(true)
                .setLongPressReleaseListener(this)
                .setOnHoverListener(this)
                .setPopupListener(this)
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                .build();

        popup.register();

//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////                Toast.makeText(getApplicationContext(), ""+imageModel.getImageName(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }


    private void getIncomingIntent() {
        if (imageModel.getImageUrl() != null) {
            String imageUrl = imageModel.getImageUrl();
            setImage(imageUrl);
        }
    }

    private void setImage(String imageUrl) {
        Glide.with(this).asDrawable().load(imageUrl).into(imageView);
    }

    private void checkPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    downloadImage(imageModel.getImageName(), imageModel.getImageUrl());
                } else {
                    Toast.makeText(GalleryActivity.this, "Error 404!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void downloadImage(String fileName, String ImageUrl) {
        try {
            DownloadManager downloadManager = null;
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(ImageUrl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(fileName)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + fileName);

            downloadManager.enqueue(request);

            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Download Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        imagePopup = root.findViewById(R.id.image_popup);

    }

    @Override
    public void onHoverChanged(View view, boolean isHovered) {

    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {
        Glide.with(this).asDrawable().load(imageModel.getImageUrl()).into(imagePopup);
    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }

    @Override
    public void onClick(View v) {

    }

    private void changeLikeOnDatabase(boolean isLiked) {
        FirebaseFirestore.getInstance().collection("all").document(imageModel.getId())
                .update("isLiked", isLiked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GalleryActivity.this, "You " + String.format("%s", isLiked ? "liked" : "disliked") + " the photo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}