package care.hospital.virtual.virtualhospital;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentListFragment extends Fragment implements OnClickListener, AdapterView.OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "access_token";

    private  static final int PENDING = 1;
    private  static final int CONFIRMED = 2;

    private OnFragmentInteractionListener mListener;

    public AppointmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentListFragment.
     */
    public static AppointmentListFragment newInstance(String access_token) {
        AppointmentListFragment fragment = new AppointmentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, access_token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchAppointmentsData() {
        RequestParams params = new RequestParams();
        String access_token = getArguments().getString(ARG_PARAM1);
        params.put(ARG_PARAM1, access_token);
        VHRestClient.post("appointment/list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                ListView appointmentsList = (ListView) getView().findViewById(R.id.appointmentsList);
                appointmentsList.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return response.length();
                    }

                    @Override
                    public Object getItem(int position) {
                        try {
                            return  response.getJSONObject(position);
                        } catch (JSONException e) {
                            Log.e(getClass().getName(), e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    public long getItemId(int position) {
                        try {
                            return response.getJSONObject(position).getInt("id");
                        } catch (JSONException e) {
                            Log.e(this.getClass().getName(), e.getMessage());
                            return -1;
                        }
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        if(convertView == null) {
                            convertView = inflater.inflate(R.layout.appointment_layout, parent, false);
                        }
                        RelativeLayout appointmentLayout = (RelativeLayout) convertView;

                        try {
                            JSONObject appointment = response.getJSONObject(position);
                            int status = appointment.getInt("status");
                            JSONObject slot = appointment.getJSONObject("Slot");
                            String time = formatTime(slot.getString("time"));
                            String doctor = slot.getString("DoctorId");


                            if(status == PENDING) {
                                ((TextView) appointmentLayout.findViewById(R.id.appointmentStatus)).setText(R.string.appointment_status_pending);
                            } else {
                                ((TextView) appointmentLayout.findViewById(R.id.appointmentStatus)).setText(R.string.appointment_status_confirmed);
                            }

                            ((TextView) appointmentLayout.findViewById(R.id.appointmentTime)).setText(time);
                            ((TextView) appointmentLayout.findViewById(R.id.appointmentDoctor)).setText(doctor);

                        } catch (JSONException e) {
                            Log.e(this.getClass().getName(), e.getMessage());
                        }
                        return appointmentLayout;
                    }

                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private String formatTime(String timeString) {
        try {
            SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdiFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date time = sdfParser.parse(timeString);
            return sdiFormatter.format(time);
        } catch (ParseException e) {
            Log.e(getClass().getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        ListView appointmentsListView = (ListView) layout.findViewById(R.id.appointmentsList);
        appointmentsListView.setOnItemClickListener(this);
        layout.findViewById(R.id.makeAppointmentButton).setOnClickListener(this);

        fetchAppointmentsData();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAppointmentsData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.cancel_appointment);
        builder.setMessage(R.string.cancel_appointment_message);
        builder.setPositiveButton(R.string.cancel_appointment_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    RequestParams params = new RequestParams();
                    String access_token = getArguments().getString(ARG_PARAM1);
                    int appointmentId = ((JSONObject) parent.getItemAtPosition(position)).getInt("id");
                    params.put("access_token", access_token);
                    params.put("appointmentId", appointmentId);

                    VHRestClient.post("appointment/cancel", params, new TextHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            fetchAppointmentsData();
                            ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
                            Snackbar.make(getView(), responseString, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Snackbar.make(getView(), responseString, Snackbar.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
        });
        builder.create().show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        mListener.onFragmentInteraction(null);
    }
}
