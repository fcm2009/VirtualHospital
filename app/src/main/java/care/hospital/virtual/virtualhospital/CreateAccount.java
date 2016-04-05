package care.hospital.virtual.virtualhospital;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
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
        System.out.print("aaa*************************************************");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_editText);
        EditText emailEditText = (EditText) findViewById(R.id.email_editText);
        EditText passwordEditText = (EditText) findViewById(R.id.password_editText);

        JSONObject accountRequest = new JSONObject();
        try {
            accountRequest.put("name", nameEditText.getText().toString());
            accountRequest.put("email", emailEditText.getText().toString());
            accountRequest.put("password", passwordEditText.getText().toString());
            //ToDo: implement api call
        } catch (JSONException e) {
            Log.v("JSON creation error", e.getMessage());
        }
        Snackbar.make(view, R.string.snackbar_message, Snackbar.LENGTH_LONG).show();
    }
}
