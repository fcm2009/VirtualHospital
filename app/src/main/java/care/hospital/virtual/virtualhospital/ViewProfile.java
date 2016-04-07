package care.hospital.virtual.virtualhospital;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        /*SharedPreferences profile_info = getSharedPreferences("profile_info", 0);
        SharedPreferences.Editor profile_info_editor = profile_info.edit();
        profile_info_editor.putString("Username", "Awad");
        profile_info_editor.commit();

        TextView username = (TextView)findViewById(R.id.username_val);
        username.setText(profile_info.getString("Username", ""));

        TextView firstName = (TextView)findViewById(R.id.first_name_val);
        firstName.setText(profile_info.getString("First Name", ""));

        TextView lastName = (TextView)findViewById(R.id.last_name_val);
        lastName.setText(profile_info.getString("Last Name", ""));

        TextView email = (TextView)findViewById(R.id.email_val);
        email.setText(profile_info.getString("Email", ""));

        TextView phone = (TextView)findViewById(R.id.phone_val);
        phone.setText(profile_info.getString("Phone", ""));*/

    }
}
