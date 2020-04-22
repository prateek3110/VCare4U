/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.ActionCodeResult;

import java.util.ArrayList;
import java.util.Locale;

public class PrescriptionMain extends AppCompatActivity {

    private ActionBar toolbar;
    String userID;
    String patientName,patientGender,patientdiagnosis,patientid;
    String patientAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presription_main);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userid");
        patientid= bundle.getString("patientid");
        patientName = bundle.getString("patientname");
        patientAge = bundle.getString("patientage");
        patientGender = bundle.getString("patientgender");

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        toolbar.hide();
        Bundle bundle3=new Bundle();
        bundle3.putString("userid", userID);
        bundle3.putString("patientid",patientid);
        bundle3.putString("patientname",patientName);
        bundle3.putString("patientage",patientAge);
        bundle3.putString("patientgender",patientGender);
        PatientDetailsFragment fragment = new PatientDetailsFragment();
        fragment.setArguments(bundle3);
        loadFragment(fragment);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        super.onBackPressed();

    }

    public void startRecognizer(int code){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, code);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.patient_details:
                    toolbar.hide();
                    Bundle bundle1=new Bundle();
                    bundle1.putString("userid", userID);
                    bundle1.putString("patientid",patientid);
                    bundle1.putString("patientname",patientName);
                    bundle1.putString("patientage",patientAge);
                    bundle1.putString("patientgender",patientGender);
                    fragment = new PatientDetailsFragment();
                    fragment.setArguments(bundle1);
                    loadFragment(fragment);
                    return true;
                case R.id.history_prescription:
                    toolbar.show();
                    toolbar.setTitle("Patient History");
                    Bundle bundle=new Bundle();
                    bundle.putString("userid", userID);
                    bundle.putString("patientname",patientName);
                    fragment = new HistoryFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.add_prescription:
                    toolbar.setTitle("Add Prescription");
                    Intent i = new Intent(PrescriptionMain.this, PrescriptionForm.class);
                    i.putExtra("userid",userID);
                    i.putExtra("patientname",patientName);
                    startActivity(i);
            }

            return false;
        }
    };

   
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

