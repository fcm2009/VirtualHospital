package care.hospital.virtual.virtualhospital;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences profile_info;
    private SharedPreferences.Editor profile_info_editor;
    
    private TextView username;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText weight;
    private EditText height;
    private EditText age;
    private EditText other_val;
    private CheckBox diabetes;
    private CheckBox hypertension;
    private CheckBox other;

    private String token;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        SharedPreferences tokenPref = getSharedPreferences("token", 0);
        token = tokenPref.getString("access_token", "");

        profile_info = getSharedPreferences("profile_info", 0);
        profile_info_editor = profile_info.edit();

        username = (TextView)findViewById(R.id.username_val);
        username.setText(profile_info.getString("Username", ""));

        firstName = (EditText)findViewById(R.id.first_name_val);
        firstName.setText(profile_info.getString("First Name", ""));

        lastName = (EditText)findViewById(R.id.last_name_val);
        lastName.setText(profile_info.getString("Last Name", ""));

        email = (EditText)findViewById(R.id.email_val);
        email.setText(profile_info.getString("Email", ""));

        phone = (EditText)findViewById(R.id.phone_val);
        phone.setText(profile_info.getString("Phone", ""));

        weight = (EditText) findViewById(R.id.weight_val);
        weight.setText(profile_info.getString("weight", ""));

        height = (EditText) findViewById(R.id.height_val);
        height.setText(profile_info.getString("height", ""));

        age = (EditText) findViewById(R.id.age_val);
        age.setText(profile_info.getString("age", ""));

        other_val = (EditText) findViewById(R.id.other_val);
        other_val.setText(profile_info.getString("other_val", ""));

        diabetes = (CheckBox) findViewById(R.id.diabetes);
        diabetes.setChecked(profile_info.getBoolean("diabetes", false));

        hypertension = (CheckBox) findViewById(R.id.hypertension);
        hypertension.setChecked(profile_info.getBoolean("hypertension", false));

        other = (CheckBox) findViewById(R.id.other);
        other.setChecked(profile_info.getBoolean("other", false));

        if(other.isChecked())
            other_val.setVisibility(View.VISIBLE);
        else
            other_val.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.save_button).getId()){
            email = (EditText) findViewById(R.id.email_val);
            firstName = (EditText) findViewById(R.id.first_name_val);
            lastName = (EditText) findViewById(R.id.last_name_val);
            phone = (EditText) findViewById(R.id.phone_val);
            weight = (EditText) findViewById(R.id.weight_val);
            height = (EditText) findViewById(R.id.height_val);
            age = (EditText) findViewById(R.id.age_val);
            other_val = (EditText) findViewById(R.id.other_val);
            diabetes = (CheckBox) findViewById(R.id.diabetes);
            hypertension = (CheckBox) findViewById(R.id.hypertension);
            other = (CheckBox) findViewById(R.id.other);


            /*if((email.getText().toString()).equals(profile_info.getString("Email", ""))) /*&&
               (firstName.getText().toString()).equals(profile_info.getString("First Name", "")) &&
               (lastName.getText().toString()).equals(profile_info.getString("Last Name", "")) &&
               (phone.getText().toString()).equals(profile_info.getString("Phone", ""))){
                Snackbar.make(findViewById(android.R.id.content), R.string.same_email, Snackbar.LENGTH_LONG).show();
            {*/
                RequestParams param = new RequestParams();

                param.put("access_token", token);
                param.put("firstName", firstName.getText());
                param.put("lastName", lastName.getText());
                param.put("email", email.getText());
                param.put("phone", phone.getText());
                /*param.put("weight", weight.getText());
                param.put("height", height.getText());
                param.put("age", age.getText());
                param.put("diabetes", diabetes.isChecked());
                param.put("hypertension", hypertension.isChecked());
                param.put("other", other.isChecked());
                if(other.isChecked())
                    param.put("other_val", other_val.getText());*/

                VHRestClient.post("account/update", param, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Snackbar.make(findViewById(android.R.id.content), R.string.data_fetch_error + responseString, Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                profile_info_editor.putString("First Name", firstName.getText().toString());
                                profile_info_editor.putString("Last Name", lastName.getText().toString());
                                profile_info_editor.putString("Email", email.getText().toString());
                                profile_info_editor.putString("Phone", phone.getText().toString());
                                Snackbar.make(findViewById(android.R.id.content), "Profile Updated Sucesfully", Snackbar.LENGTH_LONG).show();
                            }
                        });


                profile_info_editor.putString("weight", weight.getText().toString());
                profile_info_editor.putString("height", height.getText().toString());
                profile_info_editor.putString("age", age.getText().toString());
                profile_info_editor.putBoolean("diabetes", diabetes.isChecked());
                profile_info_editor.putBoolean("hypertension", hypertension.isChecked());
                profile_info_editor.putBoolean("other", other.isChecked());
                if(other.isChecked())
                    profile_info_editor.putString("other_val", other_val.getText().toString());
                else
                    profile_info_editor.putString("other_val", "");

                profile_info_editor.commit();
           // }
            /*else {

                RequestParams param = new RequestParams();

                param.put("first name", firstName.getText());
                param.put("last name", lastName.getText());
                param.put("email", email.getText());
                param.put("phone", phone.getText());
                param.put("weight", weight.getText());
                param.put("height", height.getText());
                param.put("age", age.getText());
                param.put("diabetes", diabetes.isChecked());
                param.put("hypertension", hypertension.isChecked());
                param.put("other", other.isChecked());
                if(other.isChecked())
                    param.put("other_val", other_val.getText());
                // TODO: 12/04/16

                // TODO: 12/04/16

                profile_info_editor.putString("First Name", firstName.getText().toString());
                profile_info_editor.putString("Last Name", lastName.getText().toString());
                profile_info_editor.putString("Email", email.getText().toString());
                profile_info_editor.putString("Phone", phone.getText().toString());
                profile_info_editor.putString("weight", weight.getText().toString());
                profile_info_editor.putString("height", height.getText().toString());
                profile_info_editor.putString("age", age.getText().toString());
                profile_info_editor.putBoolean("diabetes", diabetes.isChecked());
                profile_info_editor.putBoolean("hypertension", hypertension.isChecked());
                profile_info_editor.putBoolean("other", other.isChecked());
                if(other.isChecked())
                    profile_info_editor.putString("other_val", other_val.getText().toString());
                else
                    profile_info_editor.putString("other_val", "");

                profile_info_editor.commit();
            }*/
        }
    }

    public void onCheckedboxClicked(View view) {
        if(view.getId() == findViewById(R.id.other).getId()){
            other_val = (EditText) findViewById(R.id.other_val);
            other = (CheckBox) view;
            if(other.isChecked())
                other_val.setVisibility(View.VISIBLE);

            else
                other_val.setVisibility(View.GONE);
        }
    }
}
