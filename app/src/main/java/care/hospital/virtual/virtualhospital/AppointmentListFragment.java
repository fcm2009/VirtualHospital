package care.hospital.virtual.virtualhospital;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import care.hospital.virtual.virtualhospital.util.JSONArrayAdapter;
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
public class AppointmentListFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "access_token";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String access_token;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
    public static AppointmentListFragment newInstance(String access_token, String param2) {
        AppointmentListFragment fragment = new AppointmentListFragment();
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
        VHRestClient.post("appointment/list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArrayAdapter appointmentsAdapter = new JSONArrayAdapter(AppointmentListFragment.this.getContext(), R.layout.appointment_layout, response);
                ListView appointmentsList = (ListView) getView().findViewById(R.id.appointmentsList);
                appointmentsList.setAdapter(appointmentsAdapter);
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
        View layout = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        layout.findViewById(R.id.makeAppointmentButton).setOnClickListener(this);
        return layout;
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
        mListener.onFragmentInteraction(null);
    }
}
