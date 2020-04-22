/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("e1bjy")
    Call<List<Patient>> getPatientList();
}

