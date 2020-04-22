/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginScreen extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ImageView doc;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    userID=mFirebaseUser.getUid();
                    Toast.makeText(LoginScreen.this,"You're Logged In.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginScreen.this, MainActivity.class);
                    i.putExtra("userid",mFirebaseUser.getUid());
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginScreen.this,"Please, Log in.",Toast.LENGTH_SHORT).show();
                }
            }
        };
        setContentView(R.layout.login_screen);
        doc = (ImageView) findViewById(R.id.doc);
        ButterKnife.bind(this);
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognizer(10);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
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
                    ArrayList<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command.isEmpty())
                        for(int i=0; i<command.size(); i++){
                            if(command.get(i).compareToIgnoreCase("email")==0||command.get(i).compareToIgnoreCase("e mail")==0){
                                _emailText.setActivated(true);
                                startRecognizer(20);
                            }
                            else if(command.get(i).compareToIgnoreCase("password")==0){
                                _passwordText.setActivated(true);
                                startRecognizer(30);
                            }
                            else if(command.get(i).compareToIgnoreCase("Login")==0||command.get(i).compareToIgnoreCase("Log In")==0){
                                login();
                            }
                            else if(command.get(i).compareToIgnoreCase("newaccount")==0||command.get(i).compareToIgnoreCase("new account")==0){
                                Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
                            }
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 20:
                    ArrayList<String> command2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command2.isEmpty())
                    {
                        _emailText.setText(command2.get(0).toLowerCase().replaceAll("\\s", ""));
                        startRecognizer(10);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 30:
                    ArrayList<String> command3 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command3.isEmpty())
                    {
                        _passwordText.setText(command3.get(0).toLowerCase().replaceAll("\\s", ""));
                        startRecognizer(10);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

      
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid Email address");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
        } else if (email.isEmpty()&& password.isEmpty()){
            Toast.makeText(LoginScreen.this,"You haven't entered any email and password",Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty()&& password.isEmpty())){
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginScreen.this,"Login Error, Please Try Again.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        _loginButton.setEnabled(false);

                        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Authenticating...");
                        progressDialog.show();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        onLoginSuccess();
                                        // onLoginFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                }
            });
        } else {
            Toast.makeText(LoginScreen.this,"Error Occured",Toast.LENGTH_SHORT).show();
            onLoginFailed();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent i = new Intent(LoginScreen.this, MainActivity.class);
        i.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(i);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        startRecognizer(10);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid Email address");
            valid = false;
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (email.isEmpty()&& password.isEmpty()){
            Toast.makeText(LoginScreen.this,"You haven't entered any email and password",Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty()&& password.isEmpty())){
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginScreen.this,"Login Error, Please Try Again.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(LoginScreen.this,MainActivity.class));
                    }
                }
            });
        } else {
            Toast.makeText(LoginScreen.this,"Error Occured",Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}