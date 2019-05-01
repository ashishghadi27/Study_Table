package com.studlarsinc.root.studytable;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class Verify extends Fragment {



  FirebaseAuth mAuth;
  private Pinview pinview;
  private String strtext;
  private Button verify;
  public Verify() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.fragment_verify, container, false);

    pinview = view.findViewById(R.id.pinview);
    verify = view.findViewById(R.id.codeverify);
    mAuth = FirebaseAuth.getInstance();
    strtext=getArguments().getString("message");
    Log.v("CODE", strtext+"");

    verify.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        verifySignInCode();
      }
    });

    return view;
  }

  private void verifySignInCode(){
    String code = pinview.getValue();
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(strtext, code);
    signInWithPhoneAuthCredential(credential);
  }


  private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              //here you can open new activity
              Toast.makeText(getContext(),
                  "Login Successfull", Toast.LENGTH_LONG).show();
            } else {
              if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getContext(),
                    "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
              }
            }
          }
        });
  }



}
