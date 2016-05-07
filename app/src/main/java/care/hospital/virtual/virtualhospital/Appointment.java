package care.hospital.virtual.virtualhospital;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Appointment extends AppCompatActivity implements AppointmentListFragment.OnFragmentInteractionListener, MakeAppointmentFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appointmentToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.appointment_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.appointment_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("access_token", null);

        AppointmentListFragment appointmentListFragment = AppointmentListFragment.newInstance(token);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction appointmentListTransaction = fragmentManager.beginTransaction();
        appointmentListTransaction.add(android.R.id.content, appointmentListFragment);
        appointmentListTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.appointment_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity(new Intent(this, ViewProfile.class));

        } else if (id == R.id.appointment) {


        } else if (id == R.id.health_record) {
            startActivity(new Intent(this, UpdateHealthRecord.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.appointment_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        MakeAppointmentFragment makeAppointmentFragment = MakeAppointmentFragment.newInstance(token);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction MakeAppointmentTransaction = fragmentManager.beginTransaction();
        MakeAppointmentTransaction.replace(android.R.id.content, makeAppointmentFragment);
        MakeAppointmentTransaction.addToBackStack(null);
        MakeAppointmentTransaction.commit();
    }
}
