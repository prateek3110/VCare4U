/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder> {
    Context context;
    List<Prescription> prescriptionList;
    private PrescriptionAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView subpdiagnosis,subpprescription,subpadvice,subptimestamp;
        public ImageButton send;

        public MyViewHolder(View view) {
            super(view);
            subpdiagnosis = (TextView) view.findViewById(R.id.subpdiagnosis);
            subpprescription = view.findViewById(R.id.subpprescription);
            subpadvice = view.findViewById(R.id.subpadvice);
            subptimestamp = view.findViewById(R.id.subptimestamp);
            send = (ImageButton) view.findViewById(R.id.send);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    }


    public PrescriptionAdapter(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Prescription prescription = prescriptionList.get(position);
        holder.subpdiagnosis.setText(prescription.getdiagnosis());
        holder.subpprescription.setText(prescription.getprescription());
        holder.subpadvice.setText(prescription.getadvice());
        holder.subptimestamp.setText(prescription.getTimestamp());
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(PrescriptionAdapter.MyViewHolder holder, final int position) {
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public interface PrescriptionAdapterListener {
        void onIconClicked(int position);
    }
//TODO : Improvise logic for pdf making
    private void createPdf(int Position){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Prescription curr = prescriptionList.get(Position);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Patient ID - "+curr.getpatientid() + " Name - "+ curr.getpatientname(),10,10,paint);
        canvas.drawText("Age - "+curr.getage(),10,30,paint);
        /*for(int i=0; i<8; i++){
            switch (i){
                case 0 : canvas.drawText("Patient ID - "+curr.getpatientid(),10,50,paint);
                    break;
                case 1 : paint.breakText()
                    canvas.drawText("Name - " + curr.getpatientname(), 200, 1000,paint);
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
        }*/
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

        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
        }
        // close the document
        document.close();
    }
}
