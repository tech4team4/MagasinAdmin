package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class Image_Profile_Editing extends AppCompatActivity {
    ImageView imageView;
    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    Uri resultUri;
    ProgressBar progressBar;
    Button save;
    private ProgressDialog loadingBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__profile__editing);
        imageView = findViewById(R.id.image_cover);
        progressBar=findViewById(R.id.progress_bar);
        save=findViewById(R.id.sauv);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images_magasin");
        loadingBar = new ProgressDialog(this);

        storageReference = storage.getReferenceFromUrl("gs://deliveryapk-a5b8f.appspot.com/Images_magasin").child(currentUser.getUid()+".jpg");
        RetrieveUserInfo();
    }

    public void Delete_image(View view) {
    }

    public void Insert_image(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(2, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    private void FileUploader(Uri imguri) {
        /////le nom du photo
        final StorageReference Ref = mStorageRef.child(currentUser.getUid()+".jpg");
        uploadTask = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        loadingBar.dismiss();
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        Log.d("HIIIIIIII", "onSuccess: uri= " + downloadUrl.toString());
                        Toast.makeText(Image_Profile_Editing.this, "Ajoutée Avec Succées", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        loadingBar.dismiss();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    public void Sauvegarder(View view) {
        if (resultUri != null) {
            save.setEnabled(false);
            save.setBackgroundColor(Color.parseColor("#808080"));
            //resultUri = result.getUri();
            //imageView.setImageURI(resultUri);
            if (uploadTask != null && uploadTask.isInProgress()) {
                //progressBar.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
            } else {
                //progressBar.setVisibility(View.VISIBLE);
                loadingBar.setTitle("Préparer l'image");
                loadingBar.setMessage("un moment, preparation de l'image...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                FileUploader(resultUri);
            }
            //FileUploader(resultUri);
        }
    }

    private void RetrieveUserInfo() {
        final File file;
        try {
            file = File.createTempFile("image", "jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile((file.getAbsolutePath()));
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(Profile.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //RetrieveUserInfo();
    }
}
