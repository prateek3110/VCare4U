/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientForm extends AppCompatActivity {

    EditText mPatientid,mName,mAge,mGender,mMobile;
    TextView addpatient;
    Button mSave;
    DatabaseReference mDatabaseReference;
    Patient mPatient;
    String currentDateandTime,userID;
    ArrayList<String> patientID,patientName,patientAge,patientGender,patientmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userid");
        setContentView(R.layout.patientform);
        addpatient=(TextView)findViewById(R.id.add_patient);
        mPatientid = (EditText)findViewById(R.id.patient_id);
        mName = (EditText)findViewById(R.id.name);
        mAge = (EditText) findViewById(R.id.age);
        mGender = (EditText) findViewById(R.id.gender);
        mMobile = (EditText)findViewById(R.id.mobile);
        mSave = (Button) findViewById(R.id.save);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        currentDateandTime = sdf.format(currentTime);
        mPatient = new Patient();

        addpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognizer(60);
            }
        });
        startRecognizer(60);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Patient");
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long phone = Long.parseLong(mMobile.getText().toString().trim());
                mPatient.setpatientid(Integer.parseInt(mPatientid.getText().toString().trim()));
                mPatient.setpatientname(mName.getText().toString().trim());
                mPatient.setage(mAge.getText().toString().trim());
                mPatient.setgender(mGender.getText().toString().trim());
                mPatient.setmobile(phone);
                mPatient.setTimestamp(currentDateandTime);
                mPatient.setPicture(mName.getText().toString().substring(0,1).trim());
                mDatabaseReference.push().setValue(mPatient);
                startActivity(new Intent(PatientForm.this, MainActivity.class));
            }
        });
    }
    public void Save(){
        Long phone = Long.parseLong(mMobile.getText().toString().trim());
        mPatient.setpatientid(Integer.parseInt(mPatientid.getText().toString().trim()));
        mPatient.setpatientname(mName.getText().toString().trim());
        mPatient.setage(mAge.getText().toString().trim());
        mPatient.setgender(mGender.getText().toString().trim());
        mPatient.setmobile(phone);
        mPatient.setTimestamp(currentDateandTime);
        mPatient.setPicture(mName.getText().toString().substring(0,1).trim());
        mDatabaseReference.push().setValue(mPatient);
        startActivity(new Intent(PatientForm.this, MainActivity.class));
    }
    public void startRecognizer(int code){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, code);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 10:
                    patientID = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientID.isEmpty()){
                        mPatientid.setText(patientID.get(0));
                        startRecognizer(60);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 20:
                    patientName = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientName.isEmpty()){
                        mName.setText(patientName.get(0));
                        startRecognizer(60);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 30:
                    patientAge = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientAge.isEmpty()){
                        mAge.setText(patientAge.get(0));
                        startRecognizer(60);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 40:
                    patientGender = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientGender.isEmpty()){
                        if(patientGender.get(0).compareToIgnoreCase("mail")==0)
                        mGender.setText("Male");
                        else if(patientGender.get(0).compareToIgnoreCase("female")==0)
                            mGender.setText("Female");
                        else mGender.setText(patientGender.get(0));
                        startRecognizer(60);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 50:
                    patientmobile = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientmobile.isEmpty()){
                        mMobile.setText(patientmobile.get(0));
                        startRecognizer(60);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 60:
                    ArrayList<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command.isEmpty()){
                        for(int i=0; i<command.size(); i++){
                            if(command.get(i).compareToIgnoreCase("Save")==0){
                                Long phone = Long.parseLong(mMobile.getText().toString().trim());
                                mPatient.setpatientid(Integer.parseInt(mPatientid.getText().toString().trim()));
                                mPatient.setpatientname(mName.getText().toString().trim());
                                mPatient.setage(mAge.getText().toString().trim());
                                mPatient.setgender(mGender.getText().toString().trim());
                                mPatient.setmobile(phone);
                                mPatient.setTimestamp(currentDateandTime);
                                mPatient.setPicture(mName.getText().toString().substring(0,1).trim());
                                mDatabaseReference.push().setValue(mPatient);
                                startActivity(new Intent(PatientForm.this, MainActivity.class));
                            }
                            else if(command.get(i).compareToIgnoreCase("name")==0){
                                mName.setActivated(true);
                                startRecognizer(20);
                            }
                            else if(command.get(i).compareToIgnoreCase("patientid")==0||command.get(i).compareToIgnoreCase("patient id")==0){
                                mPatientid.setActivated(true);
                                startRecognizer(10);
                            }
                            else if(command.get(i).compareToIgnoreCase("age")==0){
                                mAge.setActivated(true);
                                startRecognizer(30);
                            }
                            else if(command.get(i).compareToIgnoreCase("gender")==0){
                                mGender.setActivated(true);
                                startRecognizer(40);
                            }
                            else if(command.get(i).compareToIgnoreCase("mobile")==0){
                                mMobile.setActivated(true);
                                startRecognizer(50);
                            }
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
}
