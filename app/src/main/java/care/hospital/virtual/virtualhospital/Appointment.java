package care.hospital.virtual.virtualhospital;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Appointment extends AppCompatActivity implements AppointmentListFragment.OnFragmentInteractionListener, MakeAppointment.OnFragmentInteractionListener {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        SharedPreferences sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("access_token", null);

        AppointmentListFragment appointmentListFragment = AppointmentListFragment.newInstance(token);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction appointmentListTransaction = fragmentManager.beginTransaction();
        appointmentListTransaction.replace(android.R.id.content, appointmentListFragment);
        appointmentListTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        MakeAppointment makeAppointmentFragment = MakeAppointment.newInstance(token);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction MakeAppointmentTransaction = fragmentManager.beginTransaction();
        MakeAppointmentTransaction.replace(android.R.id.content, makeAppointmentFragment);
        MakeAppointmentTransaction.addToBackStack(null);
        MakeAppointmentTransaction.commit();
    }
}
