/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class HistoryFragment extends Fragment implements PrescriptionAdapter.PrescriptionAdapterListener {

    private static final String TAG = HistoryFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<Prescription> itemsList;
    List<Prescription> prescriptionList = new ArrayList<>();
    private PrescriptionAdapter mAdapter;
    DatabaseReference mDatabaseReference;
    String userID,patientname;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        userID = getArguments().getString("userid");
        patientname = getArguments().getString("patientname");

        recyclerView = view.findViewById(R.id.pres_recycler_view);
        itemsList = new ArrayList<>();
        getPrescriptionList();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    private void getPrescriptionList(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Prescription").child(patientname);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Prescription prescription = dataSnapshot1.getValue(Prescription.class);
                    prescriptionList.add(prescription);
                }
                mAdapter = new PrescriptionAdapter(getActivity(), prescriptionList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Oops....Something is Wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onIconClicked(int position) {
        Prescription current = prescriptionList.get(position);
        createPdf(current);
    }



    private void createPdf(Prescription curr){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for(int i=0; i<8; i++){
            switch (i){
                case 0 : canvas.drawText("Patient ID - "+curr.getpatientid(),80,50,paint);
                break;
                case 1 : canvas.drawText("Name - " + curr.getpatientname(), 80, 50,paint);
                break;
                case 2 : canvas.drawText("Age - "+ Integer.toString(curr.getage(),10),80,50 ,paint);
                break;
                case 3 : canvas.drawText("Gender - " + curr.getgender(),80,50,paint);
                break;
                case 4 : canvas.drawText("Symptoms - "+curr.getsymptoms(),80,50,paint);
                break;
                case 5 : canvas.drawText("Diagnosis - " + curr.getdiagnosis(),80,50,paint);
                break;
                case 6 : canvas.drawText("Prescription - "+curr.getprescription(),80,50,paint);
                break;
                case 7 : canvas.drawText("Advice - "+curr.getadvice(),80,50,paint);
                break;
            }
        }
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"/test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

}

