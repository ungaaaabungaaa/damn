package com.example.logintest;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class eyeupload extends AppCompatActivity  implements BSImagePicker.OnSingleImageSelectedListener,  BSImagePicker.ImageLoaderDelegate,BSImagePicker.OnSelectImageCancelledListener{
    EditText mEditTextFileName;
    ImageView mImageView;
    ProgressBar mProgressBar;
    Uri mImageUri;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    StorageTask mUploadTask;
    FloatingActionButton mButtonUpload;
    Uri compreeseduri;
    File file;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_eyeupload );
        mImageView = findViewById( R.id.image_view );
        ConstraintLayout constraintLayout = findViewById( R.id.layout );
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        mEditTextFileName = findViewById( R.id.edit_text_file_name );
        mProgressBar = findViewById( R.id.progress_bar );
        mStorageRef = FirebaseStorage.getInstance().getReference("stag");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference( "stag");
        openFileChooser();
        mButtonUpload = findViewById(R.id.button_upload);
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(eyeupload.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
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


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType( cR.getType(uri));
    }




    private void uploadFile() {
        if (compreeseduri!=null) {
           mProgressBar.setVisibility(View.VISIBLE);
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(compreeseduri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(eyeupload.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(eyeupload.this, eye.class);
                            startActivity(intent);
                            finish();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            file.delete();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(eyeupload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(eyeupload.this, eye.class);
                            startActivity(intent);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            file.delete();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Picasso.get().load(imageUri).into(ivImage);
    }


    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        CropImage.activity(uri)
                .start(eyeupload.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult( data );
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mImageView.setImageURI(mImageUri);
                 file=new File(SiliCompressor.with(this).compress(FileUtils.getPath(this,mImageUri),new File(this.getCacheDir(),"temp")));
                 compreeseduri=Uri.fromFile(file);

            } else
            {
                Intent intent = new Intent(eyeupload.this, eye.class);
                startActivity(intent);
                finish();
                file.delete();
                Toast toast = Toast.makeText(eyeupload.this, "No Image selected", Toast.LENGTH_LONG); toast.show();
            }
                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(eyeupload.this, "Error : " + error, Toast.LENGTH_LONG).show();
                file.delete();
            }
        }



    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        Intent intent = new Intent(eyeupload.this, eye.class);
        startActivity(intent);
        finish();
        file.delete();
        Toast toast = Toast.makeText(eyeupload.this, "No Image selected", Toast.LENGTH_LONG); toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(eyeupload.this, eye.class);
        startActivity(intent);
        finish();
        file.delete();
        Toast toast = Toast.makeText(eyeupload.this, "No Image selected", Toast.LENGTH_LONG); toast.show();

    }
}
