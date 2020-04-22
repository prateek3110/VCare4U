/* Created By : Prateek Sharma
 ******IIT(ISM) Dhanbad******/
package prateek.project.voiceprescription;

import android.content.Context;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Patient> patients;
    private PatientAdapterListener listener;
    private SparseBooleanArray selectedItems;
    private List<String> mKeys;

    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex = -1;
    private String key;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView patient_name, txt_primary, txt_secondary, iconText, timestamp;
        public ImageView iconImp, imgProfile;
        public LinearLayout patientContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;


        public MyViewHolder(View view) {
            super(view);
            patient_name = (TextView) view.findViewById(R.id.patient_name);
            txt_primary = (TextView) view.findViewById(R.id.txt_primary);
            txt_secondary = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            patientContainer = (LinearLayout) view.findViewById(R.id.patient_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public PatientListAdapter(Context mContext, List<Patient> patients, PatientAdapterListener listener) {
        this.mContext = mContext;
        this.patients = patients;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Patient patient = patients.get(position);

        holder.patient_name.setText(patient.getpatientname());
        holder.txt_primary.setText(patient.getage());
        holder.txt_secondary.setText(patient.getgender());
        holder.timestamp.setText(patient.getTimestamp());

        holder.iconText.setText(patient.getPicture());

        holder.itemView.setActivated(selectedItems.get(position, false));

        applyReadStatus(holder, patient);

        applyImportant(holder, patient);

        applyIconAnimation(holder, position);

        applyProfilePicture(holder, patient);

        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

        holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });

        holder.patientContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPatientRowClicked(position);
            }
        });

        holder.patientContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    private void applyProfilePicture(MyViewHolder holder, Patient patient) {
        if (!TextUtils.isEmpty(patient.getPicture())) {
            Glide.with(mContext).load(patient.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(patient.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }


    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    @Override
    public long getItemId(int position) {
        return patients.get(position).getpatientid();
    }

    private void applyImportant(MyViewHolder holder, Patient patient) {
        if (patient.isImportant()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.primary_dark));
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
        }
    }

    private void applyReadStatus(MyViewHolder holder, Patient patient) {
        if (patient.isRead()) {
            holder.patient_name.setTypeface(null, Typeface.NORMAL);
            holder.txt_primary.setTypeface(null, Typeface.NORMAL);
            holder.patient_name.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            holder.txt_primary.setTextColor(ContextCompat.getColor(mContext, R.color.oil));
        } else {
            holder.patient_name.setTypeface(null, Typeface.BOLD);
            holder.txt_primary.setTypeface(null, Typeface.BOLD);
            holder.patient_name.setTextColor(ContextCompat.getColor(mContext, R.color.jet));
            holder.txt_primary.setTextColor(ContextCompat.getColor(mContext, R.color.oil));
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        patients.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface PatientAdapterListener {

        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onPatientRowClicked(int position);

        void onRowLongClicked(int position);
    }
}
