package care.hospital.virtual.virtualhospital;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Appointment extends AppCompatActivity implements View.OnClickListener,  AppointmentListFragment.OnFragmentInteractionListener, MakeAppointment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        SharedPreferences sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        AppointmentListFragment appointmentListFragment = AppointmentListFragment.newInstance(token, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction appointmentListTransaction = fragmentManager.beginTransaction();
        appointmentListTransaction.add(android.R.id.content, appointmentListFragment);
        appointmentListTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        MakeAppointment makeAppointmentFragment = MakeAppointment.newInstance(token, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction appointmentListTransaction = fragmentManager.beginTransaction();
        appointmentListTransaction.replace(android.R.id.content, makeAppointmentFragment);
        appointmentListTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
