package com.studlarsinc.root.studytable.Fragments;

import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.studlarsinc.root.studytable.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


public class Signup_details extends Fragment {


    private CircleImageView userprofileimage;
    private static final int CAMERA_REQUEST = 1888,GET_FROM_GALLERY = 27;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri filepath;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String userid, downloadurl;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup_details, container, false);
        userprofileimage = view.findViewById(R.id.user_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottom_sheet_dialog = new BottomSheetDialog(v.getContext());
                View view1 =View.inflate(v.getContext(), R.layout.profile_bottom_sheet, null);
                TextView camera = view1.findViewById(R.id.textView2);
                TextView gallery = view1.findViewById(R.id.textView1);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Objects.requireNonNull(getContext()).checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{CAMERA}, MY_CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                bottom_sheet_dialog.dismiss();
                            }
                        }
                        else{
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            bottom_sheet_dialog.dismiss();
                        }
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                        bottom_sheet_dialog.dismiss();
                    }
                });

                //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000);
                bottom_sheet_dialog.setContentView(view1);
                bottom_sheet_dialog.show();
            }
        });



        return view;
    }

    public void onActivityResult ( int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            Log.v("FILEPATH CAM", String.valueOf(filepath)+"");
            uploadFile();
            userprofileimage.setImageBitmap(photo);

        }
        else if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), selectedImage);
                filepath = selectedImage;
                Log.v("FILEPATH CAM", String.valueOf(filepath)+"");
                uploadFile();
                userprofileimage.setImageBitmap(bitmap);




            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void uploadFile() {
        //if there is a file to upload
        if (filepath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("userprofile/"+userid);
            riversRef.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        downloadurl = mStorageRef.getDownloadUrl().toString();
                        Log.v("DOWNLOAD", downloadurl);
                        //and displaying a success toast
                        Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying error message
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });



        }
        else {
        //if there is not any file
            Toast.makeText(getContext(),"File path empty", Toast.LENGTH_LONG).show();
        }

    }






}
