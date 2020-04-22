/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.view.ActionMode;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PatientListAdapter.PatientAdapterListener, NavigationView.OnNavigationItemSelectedListener {
    private List<Patient> patients = new ArrayList<>();
    private RecyclerView recyclerView;
    private PatientListAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private androidx.appcompat.view.ActionMode actionMode;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    FirebaseAuth mFirebaseAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference mDatabaseReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userid");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fn_permission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PatientForm.class);
                i.putExtra("userid",userID);
                startActivity(i);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        actionModeCallback = new ActionModeCallback();

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                       getPatientList2();
                    }
                }
        );
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, 10);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void getPatientList() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Patient>> call = apiService.getPatientList();
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                
                patients.clear();

                for (Patient patient : response.body()) {
                    patient.setColor(getRandomMaterialColor("400"));
                    patients.add(patient);
                }

                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getPatientList2(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Patient");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Patient patient = dataSnapshot1.getValue(Patient.class);
                    patients.add(patient);
                }
                mAdapter = new PatientListAdapter(MainActivity.this, patients,MainActivity.this);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Oops....Something is Wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
// TODO:Improvise Navigation Bar
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logout){
            onLogout();
        }
        else if (id == R.id.nav_home) {
           
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onLogout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginScreen.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onRefresh() {
        finish();
    }

    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode((androidx.appcompat.view.ActionMode.Callback) actionModeCallback);
        }

        toggleSelection(position);
    }


    public void onIconImportantClicked(int position) {
        Patient patient = patients.get(position);
        patient.setImportant(!patient.isImportant());
        patients.set(position, patient);
        mAdapter.notifyDataSetChanged();
    }


    public void onPatientRowClicked(int position) {
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            Patient patient = patients.get(position);
            patient.setRead(true);
            patients.set(position, patient);
            mAdapter.notifyDataSetChanged();
            Intent i = new Intent(MainActivity.this, PrescriptionMain.class);
            i.putExtra("userid",userID);
            i.putExtra("patientid",patient.getpatientid());
            i.putExtra("patientname",patient.getpatientname());
            i.putExtra("patientage",patient.getage());
            i.putExtra("patientgender",patient.getgender());
            startActivity(i);
        }
    }

    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deletePatients();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    private void deletePatients() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            String temp = patients.get(selectedItemPositions.get(i)).getpatientname();
            mAdapter.removeData(selectedItemPositions.get(i));
            Query query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("patientname").equalTo(temp);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        dataSnapshot1.getRef().removeValue();
                        onRefresh();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        mAdapter.notifyDataSetChanged();
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
                            if(command.get(i).compareToIgnoreCase("Logout")==0||command.get(i).compareToIgnoreCase("Log Out")==0){
                                onLogout();
                            }
                            else if(command.get(i).compareToIgnoreCase("addpatient")==0||command.get(i).compareToIgnoreCase("add patient")==0){
                                Intent intent = new Intent(MainActivity.this,PatientForm.class);
                                intent.putExtra("userid",userID);
                                startActivity(intent);
                            }
                            else {
                                for (int j = 0; j < patients.size(); j++) {
                                    Patient tpatient = patients.get(j);
                                    if (tpatient.getpatientname().compareToIgnoreCase(command.get(i)) == 0) {
                                        Intent intent = new Intent(MainActivity.this, PrescriptionMain.class);
                                        intent.putExtra("userid", userID);
                                        intent.putExtra("patientid", tpatient.getpatientid());
                                        intent.putExtra("patientname", tpatient.getpatientname());
                                        intent.putExtra("patientage", tpatient.getage());
                                        intent.putExtra("patientgender", tpatient.getgender());
                                        Toast.makeText(getApplicationContext(), command.get(i), Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 20:
                    ArrayList<String> command2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!command2.isEmpty()){
                        for(int i=0; i<command2.size(); i++){
                            if(command2.get(i).compareToIgnoreCase("addpatient")==0||command2.get(i).compareToIgnoreCase("add patient")==0){
                                Intent intent = new Intent(MainActivity.this,PatientForm.class);
                                intent.putExtra("userid",userID);
                                startActivity(intent);
                            }
                        }
                    }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
}
