package care.hospital.virtual.virtualhospital;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText weight;
    private EditText height;
    private EditText age;
    private EditText other_val;
    private CheckBox diabetes;
    private CheckBox hypertension;
    private CheckBox other;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editor = getSharedPreferences("profile_info", 0).edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_editText);
        EditText emailEditText = (EditText) findViewById(R.id.email_placeholder);
        EditText passwordEditText = (EditText) findViewById(R.id.password_editText);

        weight = (EditText) findViewById(R.id.weight_val);
        height = (EditText) findViewById(R.id.height_val);
        age = (EditText) findViewById(R.id.age_val);
        other_val = (EditText) findViewById(R.id.other_val);
        diabetes = (CheckBox) findViewById(R.id.diabetes);
        hypertension = (CheckBox) findViewById(R.id.hypertension);
        other = (CheckBox) findViewById(R.id.other);

        RequestParams accountRequest = new RequestParams();
        accountRequest.put("username", nameEditText.getText().toString());
        accountRequest.put("email", emailEditText.getText().toString());
        accountRequest.put("password", passwordEditText.getText().toString());
        accountRequest.put("weight", weight.getText().toString());
        accountRequest.put("height", height.getText().toString());
        accountRequest.put("age", age.getText().toString());
        accountRequest.put("diabetes", diabetes.isChecked());
        accountRequest.put("hypertension", hypertension.isChecked());
        if(other.isChecked())
            accountRequest.put("chronicDiseases", other_val.getText().toString());
        else
            accountRequest.put("chronicDiseases", "");

        VHRestClient.post("account/create", accountRequest, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Snackbar.make(findViewById(android.R.id.content), R.string.error + responseString, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                editor.putString("weight", weight.getText().toString());
                editor.putString("height", height.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putBoolean("diabetes", diabetes.isChecked());
                editor.putBoolean("hypertension", hypertension.isChecked());
                editor.putBoolean("other", other.isChecked());
                if(other.isChecked())
                    editor.putString("other_val", other_val.getText().toString());
                else
                    editor.putString("other_val", "");

                editor.commit();
                Snackbar.make(findViewById(android.R.id.content), R.string.account_created_successfully, Snackbar.LENGTH_LONG).show();


            }
        });

        /*@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                editor.putString("weight", weight.getText().toString());
                editor.putString("height", height.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putBoolean("diabetes", diabetes.isChecked());
                editor.putBoolean("hypertension", hypertension.isChecked());
                editor.putBoolean("other", other.isChecked());
                if(other.isChecked())
                    editor.putString("other_val", other_val.getText().toString());
                else
                    editor.putString("other_val", "");

                editor.commit();
                Snackbar.make(findViewById(android.R.id.content), R.string.account_created_successfully, Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Snackbar.make(findViewById(android.R.id.content), R.string.error, Snackbar.LENGTH_LONG).show();
                //TODO: specify message for each status code
            }
        });*/
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
