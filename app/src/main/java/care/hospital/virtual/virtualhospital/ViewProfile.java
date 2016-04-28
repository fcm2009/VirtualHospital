package care.hospital.virtual.virtualhospital;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences profile_info;
    private SharedPreferences.Editor profile_info_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profile_info = getSharedPreferences("profile_info", 0);
        profile_info_editor = profile_info.edit();

        TextView username = (TextView)findViewById(R.id.username_val);
        username.setText(profile_info.getString("Username", ""));

        EditText firstName = (EditText)findViewById(R.id.first_name_val);
        firstName.setText(profile_info.getString("First Name", ""));

        EditText lastName = (EditText)findViewById(R.id.last_name_val);
        lastName.setText(profile_info.getString("Last Name", ""));

        EditText email = (EditText)findViewById(R.id.email_val);
        email.setText(profile_info.getString("Email", ""));

        EditText phone = (EditText)findViewById(R.id.phone_val);
        phone.setText(profile_info.getString("Phone", ""));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.save_button).getId()){
            EditText email = (EditText) findViewById(R.id.email_val);
            EditText firstName = (EditText) findViewById(R.id.first_name_val);
            EditText lastName = (EditText) findViewById(R.id.last_name_val);
            EditText phone = (EditText) findViewById(R.id.phone_val);


            if((email.getText().toString()).equals(profile_info.getString("Email", "")) &&
               (firstName.getText().toString()).equals(profile_info.getString("First Name", "")) &&
               (lastName.getText().toString()).equals(profile_info.getString("Last Name", "")) &&
               (phone.getText().toString()).equals(profile_info.getString("Phone", ""))){
                Snackbar.make(findViewById(android.R.id.content), R.string.same_email, Snackbar.LENGTH_LONG).show();
            }
            else {

                RequestParams param = new RequestParams();

                param.put("first name", firstName.getText());
                param.put("last name", lastName.getText());
                param.put("email", email.getText());
                param.put("phone", phone.getText());
                // TODO: 12/04/16

                // TODO: 12/04/16

                profile_info_editor.putString("First Name", firstName.getText().toString());
                profile_info_editor.putString("Last Name", lastName.getText().toString());
                profile_info_editor.putString("Email", email.getText().toString());
                profile_info_editor.putString("Phone", phone.getText().toString());
                profile_info_editor.commit();
            }


        }
        
        else{
            // TODO: 12/04/16  
        }
    }
}
