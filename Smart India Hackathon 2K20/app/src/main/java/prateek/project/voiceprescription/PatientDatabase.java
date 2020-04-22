/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientDatabase {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Patient> patients = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Patient> patients,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public PatientDatabase(){
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Patient");
    }

    public void readPatients(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Patient patient = keyNode.getValue(Patient.class);
                    patients.add(patient);
                }
                dataStatus.DataIsLoaded(patients,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
