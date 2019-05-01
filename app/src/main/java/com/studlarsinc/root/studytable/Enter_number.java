package com.studlarsinc.root.studytable;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Enter_number extends Fragment {

  private EditText phone;
  private Button verify;
  private String codesent;

  private FirebaseAuth mAuth;
  public Enter_number() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.fragment_enter_number, container, false);

    phone = view.findViewById(R.id.phone_number);
    verify = view.findViewById(R.id.verify);
    mAuth = FirebaseAuth.getInstance();


    verify.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        sendVerfication();
      }
    });

    return view;
  }

  private void sendVerfication(){

    String phoneNumber = phone.getText().toString();
    phoneNumber = phoneNumber.replaceAll(" ","");



    if(!phoneNumber.isEmpty() && phoneNumber.length()==10 ){
      phoneNumber = "+91" + phoneNumber;
      PhoneAuthProvider.getInstance().verifyPhoneNumber(
          phoneNumber,        // Phone number to verify
          60,                 // Timeout duration
          TimeUnit.SECONDS,   // Unit of timeout
          (Activity) Objects.requireNonNull(getContext()),               // Activity (for callback binding)
          mCallbacks);
    }
    else {
      phone.setError("Enter Proper Phone Number");
      phone.requestFocus();
    }


  }

  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {

    }

    @Override
    public void onCodeSent(String s, ForceResendingToken forceResendingToken) {
      super.onCodeSent(s, forceResendingToken);
      Log.v("Code",s);
      Bundle bundle=new Bundle();
      bundle.putString("message", s);
      //set Fragmentclass Arguments
      Verify fragobj=new Verify();
      fragobj.setArguments(bundle);
      loadFragment(fragobj);

    }
  };

  public void loadFragment(android.support.v4.app.Fragment fragment) {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

}
