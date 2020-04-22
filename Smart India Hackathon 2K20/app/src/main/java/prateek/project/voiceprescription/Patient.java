/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

public class Patient {
    private int mPatientid;
    private String mPatientname;
    private String mAge;
    private String mGender;
    private Long mMobile;
    private String mlastvisit;
    private String mpicture;
    private boolean isImportant;
    private boolean isRead;
    private int color = -1;

    public Patient() {
    }

    public int getpatientid() {
        return mPatientid;
    }
    public String getpatientname() {
        return mPatientname;
    }
    public String getage() {
        return mAge;
    }

    public String getgender() {
        return mGender;
    }
    public Long getmobile() {
        return mMobile;
    }

    public String getTimestamp() {
        return mlastvisit;
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
    public void setage(String age) {
        this.mAge = age;
    }

    public void setgender(String gender) {
        this.mGender = gender;
    }

    public void setmobile(Long mobile) {
        this.mMobile = mobile;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getPicture() {
        return mpicture;
    }

    public void setPicture(String picture) {
        this.mpicture = picture;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

