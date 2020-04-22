/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

public class Prescription {
    private int mPatientid;
    private String mPatientname;
    private int mAge;
    private String mGender;
    private String mSymptoms;
    private String mDiagnosis;
    private String mPrescription;
    private String mAdvice;
    private String mlastvisit;

    public Prescription() {
    }

    public int getpatientid() {
        return mPatientid;
    }
    public String getpatientname() {
        return mPatientname;
    }
    public int getage() {
        return mAge;
    }

    public String getgender() {
        return mGender;
    }
    public String getTimestamp() {
        return mlastvisit;
    }

    public String getsymptoms() {
        return mSymptoms;
    }
    public String getdiagnosis() {
        return mDiagnosis;
    }
    public String getprescription() {
        return mPrescription;
    }
    public String getadvice() {
        return mAdvice;
    }

    public void setTimestamp(String lastvisit) {
        this.mlastvisit = lastvisit;
    }
    public void setpatientid(int patientid) {
        this.mPatientid = patientid;
    }
    public void setpatientname(String patientname) {
        this.mPatientname = patientname;
    }
    public void setage(int age) {
        this.mAge = age;
    }

    public void setgender(String gender) {
        this.mGender = gender;
    }

    public void setsymptoms(String symptoms) {
        this.mSymptoms = symptoms;
    }

    public void setdiagnosis(String diagnosis) {
        this.mDiagnosis = diagnosis;
    }

    public void setprescription(String prescription) {
        this.mPrescription = prescription;
    }

    public void setadvice(String advice) {
        this.mAdvice = advice;
    }
}


