package com.example.logintest;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;
public class newpostactivity extends AppCompatActivity  implements BSImagePicker.OnSingleImageSelectedListener,  BSImagePicker.ImageLoaderDelegate,  BSImagePicker.OnSelectImageCancelledListener{
    private ImageView newPostImage;
    private EditText newPostDesc;
    private FloatingActionButton newPostBtn;
    private Uri postImageUri = null;
    private ProgressBar newPostProgress;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private Bitmap compressedImageFile;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_newpostactivity );
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
        ConstraintLayout constraintLayout = findViewById( R.id.relativeLayout );
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        openFileChooser();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();
        newPostImage = findViewById( R.id.new_post_image );
        newPostDesc = findViewById( R.id.new_post_desc );
        newPostBtn = findViewById( R.id.post_btn );
        newPostProgress = findViewById( R.id.new_post_progress );
        newPostImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        } );
        newPostBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = newPostDesc.getText().toString();
                if (!TextUtils.isEmpty( desc ) && postImageUri != null) {
                    newPostProgress.setVisibility( View.VISIBLE );
                    final String randomName = UUID.randomUUID().toString();
                    File newImageFile = new File(postImageUri.getPath());
                    try {
                        compressedImageFile = new Compressor( newpostactivity.this )
                                .setQuality( 65 )
                                .compressToBitmap( newImageFile );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress( Bitmap.CompressFormat.JPEG, 65, baos );
                    byte[] imageData = baos.toByteArray();
                    UploadTask filePath = storageReference.child( "All" ).child( randomName + ".jpg" ).putBytes( imageData );
                    filePath.addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                            final String downloadUri = task.getResult().toString();
                            if (task.isSuccessful()) {
                                File newThumbFile = new File( postImageUri.getPath() );
                                try {
                                    compressedImageFile = new Compressor( newpostactivity.this )
                                            .setMaxHeight( 100 )
                                            .setMaxWidth( 100 )
                                            .setQuality( 1 )
                                            .compressToBitmap( newThumbFile );
                                } catch (IOException e) {
                                    e.printStackTrace(); }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress( Bitmap.CompressFormat.JPEG, 100, baos );
                                byte[] thumbData = baos.toByteArray();
                                UploadTask uploadTask = storageReference.child( "post_images/thumbs" )
                                        .child( randomName + ".jpg" ).putBytes( thumbData );
                                uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String downloadthumbUri =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put( "image_url", downloadUri );
                                        postMap.put( "image_thumb", downloadthumbUri );
                                        postMap.put( "desc", desc );
                                        postMap.put( "user_id", current_user_id );
                                        postMap.put( "timestamp", FieldValue.serverTimestamp() );
                                        firebaseFirestore.collection( "Posts" ).add( postMap ).addOnCompleteListener( new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText( newpostactivity.this, "Post was added", Toast.LENGTH_LONG ).show();
                                                    Intent mainIntent = new Intent( newpostactivity.this, home.class );
                                                    startActivity( mainIntent );
                                                    finish();
                                                } else {

                                                }newPostProgress.setVisibility( View.INVISIBLE );
                                            }
                                        } );
                                    }
                                } ).addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(newpostactivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                            else {
                                newPostProgress.setVisibility( View.INVISIBLE );
                              }
                        }
                    });
                }
            }
        });
    }



    private void openFileChooser() {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.example.filepicker.fileprovider")
                .setMaximumDisplayingImages(23)
                .setSpanCount(3)
                .setGridSpacing(Utils.dp2px(2))
                .setPeekHeight(Utils.dp2px(360))
                .hideCameraTile()
                .build();
        singleSelectionPicker.show(getSupportFragmentManager(), "hey");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult( data );
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                newPostImage.setImageURI( postImageUri );
            } else {
                Intent intent = new Intent(newpostactivity.this, eye.class);
                startActivity(intent);
                finish();
                Toast toast = Toast.makeText(newpostactivity.this, "No Image selected", Toast.LENGTH_LONG); toast.show();
                  }
                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(newpostactivity.this, "Error : " + error, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Picasso.get().load(imageUri).into(ivImage);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        CropImage.activity(uri)
                .start(newpostactivity.this);
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        Intent intent = new Intent(newpostactivity.this, home.class);
        startActivity(intent);
        finish();
    }



}
