package care.hospital.virtual.virtualhospital;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;

public class UpdateHealthRecord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int REQUEST_FOR_FILE = 1;

    private Uri fileUri;
    private ArrayList<MedicalReports> files = new ArrayList<>();
    private ArrayAdapter <MedicalReports> adapter;
    private ProgressDialog progress;
    private String token;
    private File afile;
    private RequestParams params;
    private boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_health_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences auth = getSharedPreferences("token", 0);
        token = auth.getString("access_token", "");

        adapter = new ArrayAdapter<>(this, R.layout.textview, files);
        ListView list = (ListView)findViewById(R.id.file_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked(position);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_health_record, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCheckedboxClicked(View view) {
        if(view.getId() == findViewById(R.id.other).getId()){
            EditText other_val = (EditText) findViewById(R.id.other_val);
            CheckBox other = (CheckBox) view;
            if(other.isChecked())
                other_val.setVisibility(View.VISIBLE);

            else
                other_val.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.add_files).getId()){
            getFile();
        }
    }

    public void getFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FOR_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_FOR_FILE){
                fileUri = data.getData();
                try {
                    addFileToDir(fileUri);
                }
                catch(IOException e){}
            }
        }
    }

    public void addFileToDir(Uri uri) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        String name = UriParser.getName(this, uri);
        if (name == null)
            getFile();
        File dst = new File(getFilesDir().getAbsolutePath(), name);
        OutputStream out = openFileOutput(dst.getName(), MODE_WORLD_READABLE);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        addFileToArray(dst);
        sendFile(dst);
    }

    public void addFileToArray(File file){
        MedicalReports newReport = new MedicalReports();
        newReport.setName(file.getName());
        newReport.setReport(file);
        files.add(newReport);
        adapter.notifyDataSetChanged();
    }

    public void sendFile(File file){
        afile = file;
        progress = ProgressDialog.show(this, getString(R.string.send_file), getString(R.string.waiting), true);
        params = new RequestParams();
        try{
            params.put("access_token", token);
            params.put("new_medical_file", file);
            VHRestClient.post("medicalHistory/upload", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progress.dismiss();
                    AlertDialog.Builder resend = new AlertDialog.Builder(UpdateHealthRecord.this);
                    resend.setTitle(getString(R.string.send_file));
                    resend.setMessage(getString(R.string.failed_upload));
                    resend.setCancelable(false);
                    resend.setPositiveButton(R.string.resend_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            sendFile(afile);
                        }
                    });

                    resend.setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog resendDialog = resend.create();
                    resendDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    progress.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), R.string.success_upload, Snackbar.LENGTH_LONG).show();


                }
            });
        }
        catch(FileNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void itemClicked(int position){
        MedicalReports report = files.get(position);
        File file = report.getReport();
        if(file != null)
            viewFile(file);
        else{
            file = searchForFile(report.getName());
            if(file != null){
                report.setReport(file);
                viewFile(file);
            }
            else
                retrieveFile(position);
        }
    }

    public File searchForFile(String fileName){
        File [] dirFiles = getFilesDir().listFiles();
        for(int i = 0; i < dirFiles.length; i++)
            if(dirFiles[i].getName().equals(fileName))
                return dirFiles[i];
        return null;
    }

    public File retrieveFile (int position){
        check = true;
        MedicalReports report = files.get(position);
        afile = new File(getFilesDir(), report.getName());
        params = new RequestParams();
        params.put("access_token", token);
        params.put("retrieve_medical_file", report.getId());
        Toast.makeText(this, R.string.download, Toast.LENGTH_LONG).show();
        while(check) {
            VHRestClient.post("medicalHistory/getById", params, new FileAsyncHttpResponseHandler(afile) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.success_download, Snackbar.LENGTH_LONG).show();
                    check = false;
                }
            });
        }
        return afile;
    }

    public void viewFile(File afile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(afile), "image/png");
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, R.string.pdf_viewer, Toast.LENGTH_LONG).show();
        }
    }
}
