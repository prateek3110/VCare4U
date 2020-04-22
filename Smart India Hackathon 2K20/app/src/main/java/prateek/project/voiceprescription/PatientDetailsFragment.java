/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PatientDetailsFragment extends Fragment {

    private static final String TAG = PatientDetailsFragment.class.getSimpleName();

    TextView profileName,profileAge,profileGender,patientDiagnosis,patientID;
    String patientid,patientname,patientage,patientgender,userID;
    DatabaseReference mDatabaseReference;
    ArrayList<Prescription> prescriptionList;

    public PatientDetailsFragment() {
        // Required empty public constructor
    }

    public static PatientDetailsFragment newInstance(String param1, String param2) {
        PatientDetailsFragment fragment = new PatientDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_details, container, false);
        userID = getArguments().getString("userid");
        patientid = getArguments().getString("patientid");
        patientname = getArguments().getString("patientname");
        patientage = getArguments().getString("patientage");
        patientgender = getArguments().getString("patientgender");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        patientID = (TextView) getView().findViewById(R.id.patient_id);
        profileName = (TextView) getView().findViewById(R.id.profile_name);
        profileAge = (TextView) getView().findViewById(R.id.patient_age);
        profileGender = (TextView) getView().findViewById(R.id.patient_gender);
        patientDiagnosis = (TextView) getView().findViewById(R.id.patient_diagnosis);

        patientID.setText(patientid);
        profileName.setText(patientname);
        profileAge.setText(patientage);
        profileGender.setText(patientgender);
    }

    public void getPatientList(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Prescription").child(patientname);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Prescription prescription = dataSnapshot1.getValue(Prescription.class);
                    prescriptionList.add(prescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Oops....Something is Wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


