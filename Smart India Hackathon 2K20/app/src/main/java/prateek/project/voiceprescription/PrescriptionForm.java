/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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

public class PrescriptionForm extends AppCompatActivity implements TextToSpeech.OnInitListener{
    EditText mPatientid,mName,mAge,mGender,mSymptoms,mDiagnosis,mPrescription,mAdvice;
    Button mSave;
    DatabaseReference mDatabaseReference;
    Prescription mSubscription;
    ArrayList<String> patientName,patientID,patientAge,patientGender,patientSymptoms,patientDiagnosis,patientPrescription,patientAdvice;
    TextToSpeech textToSpeech;
    TextView addPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescriptionform);
        textToSpeech = new TextToSpeech(this, this);
        Bundle bundle = getIntent().getExtras();
        String userID = bundle.getString("userid");
        String patientname = bundle.getString("patientname");
        mPatientid = (EditText)findViewById(R.id.sub_patient_id);
        mName = (EditText)findViewById(R.id.sub_name);
        mAge = (EditText) findViewById(R.id.sub_age);
        mGender = (EditText) findViewById(R.id.sub_gender);
        mSymptoms = (EditText)findViewById(R.id.sub_symptoms);
        mDiagnosis = (EditText)findViewById(R.id.sub_diagonsis);
        mPrescription = (EditText)findViewById(R.id.sub_Prescription);
        mAdvice = (EditText) findViewById(R.id.sub_advice);
        mSave = (Button) findViewById(R.id.sub_save);
        addPrescription =(TextView) findViewById(R.id.add_prescription);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(currentTime);
        mSubscription = new Prescription();
        startRecognizer(90);

        addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognizer(90);
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Prescription").child(patientname);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int age = Integer.parseInt(mAge.getText().toString().trim());
                mSubscription.setpatientid(Integer.parseInt(mPatientid.getText().toString().trim()));
                mSubscription.setpatientname(mName.getText().toString().trim());
                mSubscription.setage(age);
                mSubscription.setgender(mGender.getText().toString().trim());
                mSubscription.setsymptoms(mSymptoms.getText().toString().trim());
                mSubscription.setdiagnosis(mDiagnosis.getText().toString().trim());
                mSubscription.setprescription(mPrescription.getText().toString().trim());
                mSubscription.setadvice(mAdvice.getText().toString().trim());
                mSubscription.setTimestamp(currentDateandTime);
                mDatabaseReference.push().setValue(mSubscription);
                startActivity(new Intent(PrescriptionForm.this, PrescriptionMain.class));
            }
        });
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
                    startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 20:
                    patientName = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientName.isEmpty()){
                        mName.setText(patientName.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 30:
                    patientAge = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientAge.isEmpty()){
                        mAge.setText(patientAge.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 40:
                    patientGender = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientGender.isEmpty()){
                        mGender.setText(patientGender.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 50:
                    patientSymptoms = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientSymptoms.isEmpty()){
                        mSymptoms.setText(patientSymptoms.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 60:
                    patientDiagnosis = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientDiagnosis.isEmpty()){
                        mDiagnosis.setText(patientDiagnosis.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 70:
                    patientPrescription = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientPrescription.isEmpty()){
                        mPrescription.setText(patientPrescription.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 80:
                    patientAdvice = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!patientAdvice.isEmpty()){
                        mAdvice.setText(patientAdvice.get(0));
                        startRecognizer(90);}
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 90:
                    ArrayList<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command.isEmpty()){
                        for(int i=0; i<command.size(); i++){
                            if(command.get(i).compareToIgnoreCase("name")==0){
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
                            else if(command.get(i).compareToIgnoreCase("symptoms")==0||command.get(i).compareToIgnoreCase("symptom")==0){
                                mSymptoms.setActivated(true);
                                startRecognizer(50);
                            }
                            else if(command.get(i).compareToIgnoreCase("diagnosis")==0){
                                mDiagnosis.setActivated(true);
                                startRecognizer(60);
                            }
                            else if(command.get(i).compareToIgnoreCase("prescription")==0){
                                mPrescription.setActivated(true);
                                startRecognizer(70);
                            }
                            else if(command.get(i).compareToIgnoreCase("advice")==0){
                                mAdvice.setActivated(true);
                                startRecognizer(80);
                            }
                        }
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
    private String getStringFromResult(ArrayList<String> results) {
        for (String str : results) {
            {
                return getStringFromText(str);
            }
        }
        return null;
    }

    private String getStringFromText(String str){
        switch (str){
            case "prateek": return "Prateek";
        }
        return null;
    }

    @Override
    public void onInit(int i) {

    }
}
