package care.hospital.virtual.virtualhospital.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import care.hospital.virtual.virtualhospital.R;

/**
 * Created by fcm2009 on 4/25/16.
 */
public class JSONArrayAdapter extends BaseAdapter {
    Context context;
    int itemViewId;
    JSONArray data;

    public JSONArrayAdapter(Context context, int itemViewId, JSONArray data) {
        super();
        this.context = context;
        this.itemViewId = itemViewId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return data.getJSONObject(position);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        try {
            return data.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout itemView;
        itemView = convertView == null ? (RelativeLayout) inflater.inflate(itemViewId, parent, false) : (RelativeLayout) convertView;

        String status;
        Date time;
        String doctor;
        try {
            status = data.getJSONObject(position).getString("status");
            JSONObject slot = data.getJSONObject(position).getJSONObject("Slot");
            doctor = slot.getString("DoctorId");

            SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdiFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            time = sdfParser.parse(slot.getString("time"));
            String timeString = sdiFormatter.format(time);

            TextView appointmentStatus = (TextView) itemView.findViewById(R.id.appointmentStatus);
            appointmentStatus.setText(status);

            TextView appointmentTime = (TextView) itemView.findViewById(R.id.appointmentTime);
            appointmentTime.setText(timeString);

            TextView appointmentDoctor = (TextView) itemView.findViewById(R.id.appointmentDoctor);
            appointmentDoctor.setText(doctor);

        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        } catch (ParseException pe) {
            Log.e(this.getClass().getName(), pe.getMessage());
        }
        return itemView;
    }
}
