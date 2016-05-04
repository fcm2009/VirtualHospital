package care.hospital.virtual.virtualhospital;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
 * {@link MakeAppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeAppointmentFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "access_token";
    private static final String AVAILABLE = "false";
    private static final String BOOKED = "true";

    private OnFragmentInteractionListener mListener;

    public MakeAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MakeAppointmentFragment.
     */
    public static MakeAppointmentFragment newInstance(String access_token) {
        MakeAppointmentFragment fragment = new MakeAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, access_token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchDoctorsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_make_appointment, container, false);
        Spinner doctorsSpinner = (Spinner) layout.findViewById(R.id.chooseDoctorSpinner);
        ListView slotsListView = (ListView) layout.findViewById(R.id.chooseSlotList);

        doctorsSpinner.setOnItemSelectedListener(this);
        slotsListView.setOnItemClickListener(this);

        return layout;
    }

    private void fetchDoctorsData() {
        RequestParams params = new RequestParams();
        String access_token = getArguments().getString(ARG_PARAM1);
        params.put(ARG_PARAM1, access_token);

        VHRestClient.post("appointment/listDoctors", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                Spinner doctorsSpinner = (Spinner) getView().findViewById(R.id.chooseDoctorSpinner);
                doctorsSpinner.setAdapter(new BaseAdapter() {
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
                            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                        }
                        TextView doctorView = (TextView) convertView;
                        JSONObject doctor = (JSONObject) getItem(position);
                        try {
                            doctorView.setText(doctor.getString("firstName") + " " + doctor.getString("lastName"));
                        } catch (JSONException e) {
                            Log.e(getClass().getName(), e.getMessage());
                        }
                        return convertView;
                    }

                });
            }
        });
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            int doctorId = ((JSONObject) parent. getItemAtPosition(position)).getInt("id");
            RequestParams params = new RequestParams();
            String access_token = getArguments().getString(ARG_PARAM1);
            params.put("access_token", access_token);
            params.put("doctorId", doctorId);

            VHRestClient.post("appointment/listSlots", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                    ListView slotsListView = (ListView) getView().findViewById(R.id.chooseSlotList);
                    slotsListView.setAdapter(new BaseAdapter() {
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
                                convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                            }
                            TwoLineListItem slotLayout = (TwoLineListItem) convertView;

                            String status;
                            Date time;
                            try {
                                status = response.getJSONObject(position).getString("status");

                                SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                SimpleDateFormat sdiFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                time = sdfParser.parse(response.getJSONObject(position).getString("time"));
                                String timeString = sdiFormatter.format(time);

                                if(status.equals(AVAILABLE)) {
                                    slotLayout.getText1().setText(R.string.available);
                                } else {
                                    slotLayout.getText1().setText(R.string.booked);
                                }
                                slotLayout.getText2().setText(timeString);

                            } catch (JSONException e) {
                                Log.e(this.getClass().getName(), e.getMessage());
                            } catch (ParseException pe) {
                                Log.e(this.getClass().getName(), pe.getMessage());
                            }
                            return slotLayout;
                        }

                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.make_appointment);
        builder.setMessage(R.string.make_appointment_message);
        builder.setPositiveButton(R.string.make_appointment_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    RequestParams params = new RequestParams();
                    String access_token = getArguments().getString(ARG_PARAM1);
                    JSONObject slot = (JSONObject) parent.getItemAtPosition(position);
                    int slotId = slot.getInt("id");
                    int doctorId = slot.getInt("DoctorId");
                    params.put("access_token", access_token);
                    params.put("slotId", slotId);
                    params.put("doctorId", doctorId);

                    VHRestClient.post("appointment/make", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            fetchDoctorsData();
                            ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
                            Snackbar.make(getView(), R.string.appointment_created_successfully, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();
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
}
