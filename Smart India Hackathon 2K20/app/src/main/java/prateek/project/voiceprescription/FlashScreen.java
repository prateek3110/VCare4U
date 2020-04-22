/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;


public class FlashScreen extends AppCompatActivity {

    private static int Flash_screen =2000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.flash_screen);

        View someView =findViewById(R.id.flash_screen);
        View root =someView.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FlashScreen.this, LoginScreen.class);
                startActivity(i);
                finish();
            }
        },Flash_screen);
    }
}
