package com.studlarsinc.root.studytable.Fragments;

import static android.Manifest.permission.CAMERA;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.studlarsinc.root.studytable.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


public class Signup_details extends Fragment {


    CircleImageView userprofileimage;
    private static final int CAMERA_REQUEST = 1888,GET_FROM_GALLERY = 27;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup_details, container, false);
        userprofileimage = view.findViewById(R.id.user_image);
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

            userprofileimage.setImageBitmap(photo);

        }
        else if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), selectedImage);
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




}
