/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupScreen extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    ImageView doc;
    FirebaseAuth mFirebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        doc = (ImageView)findViewById(R.id.doc);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognizer(10);
            }
        });
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginScreen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
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
                    ArrayList<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command.isEmpty())
                        for(int i=0; i<command.size(); i++){
                            if(command.get(i).compareToIgnoreCase("name")==0){
                                _nameText.setActivated(true);
                                startRecognizer(40);
                            }
                            else if(command.get(i).compareToIgnoreCase("Address")==0){
                                _addressText.setActivated(true);
                                startRecognizer(50);
                            }
                            else if(command.get(i).compareToIgnoreCase("email")==0||command.get(i).compareToIgnoreCase("e mail")==0){
                                _emailText.setActivated(true);
                                startRecognizer(20);
                            }
                            else if(command.get(i).compareToIgnoreCase("mobile")==0){
                                _mobileText.setActivated(true);
                                startRecognizer(70);
                            }
                            else if(command.get(i).compareToIgnoreCase("password")==0){
                                _passwordText.setActivated(true);
                                startRecognizer(30);
                            }
                            else if(command.get(i).compareToIgnoreCase("reenterpassword")==0||command.get(i).compareToIgnoreCase("re enter password")==0||command.get(i).compareToIgnoreCase("re enterpassword")==0||command.get(i).compareToIgnoreCase("reenter password")==0){
                                _reEnterPasswordText.setActivated(true);
                                startRecognizer(60);
                            }
                            else if(command.get(i).compareToIgnoreCase("sign up")==0||command.get(i).compareToIgnoreCase("signup")==0){
                                signup();
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
                case 40:
                    ArrayList<String> command4 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command4.isEmpty())
                    {
                        _nameText.setText(command4.get(0));
                        startRecognizer(10);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 50:
                    ArrayList<String> command5 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command5.isEmpty())
                    {
                        _addressText.setText(command5.get(0));
                        startRecognizer(10);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 60:
                    ArrayList<String> command6 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command6.isEmpty())
                    {
                        _reEnterPasswordText.setText(command6.get(0).toLowerCase().replaceAll("\\s", ""));
                        startRecognizer(10);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 70:
                    ArrayList<String> command7 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command7.isEmpty())
                    {
                        _mobileText.setText(command7.get(0));
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

    public void signup() {
        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupScreen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
        } else if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
        } else if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
        } else if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
        } else if (email.isEmpty()&& password.isEmpty()){
            Toast.makeText(SignupScreen.this,"You haven't entered any email and password",Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty()&& password.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupScreen.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignupScreen.this,"SignUp Unsuccessful, Please Try Again.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onSignupSuccess or onSignupFailed
                                        // depending on success
                                        onSignupSuccess();
                                        // onSignupFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                }
            });
        } else {
            Toast.makeText(SignupScreen.this,"Error Occured",Toast.LENGTH_SHORT).show();
            onSignupFailed();
            return;
        }
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent i = new Intent(SignupScreen.this,MainActivity.class);
        i.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else if (email.isEmpty()&& password.isEmpty()){
            Toast.makeText(SignupScreen.this,"You haven't entered any email and password",Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty()&& password.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupScreen.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignupScreen.this,"SignUp Unsuccessful, Please Try Again.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(SignupScreen.this,LoginScreen.class));
                    }
                }
            });
        } else {
            Toast.makeText(SignupScreen.this,"Error Occured",Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}
