package care.hospital.virtual.virtualhospital;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import care.hospital.virtual.virtualhospital.util.JSONArrayAdapter;
import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MakeAppointment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeAppointment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeAppointment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "access_token";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String access_token;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MakeAppointment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeAppointment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeAppointment newInstance(String access_token, String param2) {
        MakeAppointment fragment = new MakeAppointment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, access_token);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            access_token = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        RequestParams params = new RequestParams();
        params.put(ARG_PARAM1, access_token);
        VHRestClient.post("appointment/listDoctors", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                Spinner doctorsSpinner = (Spinner) getView().findViewById(R.id.chooseDoctorSpinner);
                BaseAdapter spinnerAdapter = new BaseAdapter() {
                    private JSONArray data = response;
                    private Context context = getContext();

                    @Override
                    public int getCount() {
                        return data.length();
                    }

                    @Override
                    public Object getItem(int position) {
                        try {
                            return  data.getJSONObject(position);
                        } catch (JSONException e) {
                            Log.e(getClass().getName(), e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        if(convertView == null) {
                            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                        }
                        TextView doctorView = (TextView) convertView;
                        doctorView.setText(getItem(position).toString());
                        return convertView;
                    }

                };
                doctorsSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_appointment, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
