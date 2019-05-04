package com.studlarsinc.root.studytable.Activities;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import com.studlarsinc.root.studytable.Fragments.Enter_number;
import com.studlarsinc.root.studytable.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    requestSmsPermission();
    loadFragment(new Enter_number());
  }

  private void requestSmsPermission() {

    // check permission is given
    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      // request permission (see result in onRequestPermissionsResult() method)
      ActivityCompat.requestPermissions(MainActivity.this,
          new String[]{permission.RECEIVE_SMS},2);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case 2: {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted
          Log.v("PERMISSION","Granted");
        } else {
          Log.v("PERMISSION","Denied");
        }
        return;
      }
    }
  }

  public void loadFragment(android.support.v4.app.Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, fragment);
    transaction.commit();
  }

}
