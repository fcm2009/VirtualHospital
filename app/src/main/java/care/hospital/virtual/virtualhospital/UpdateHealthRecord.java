package care.hospital.virtual.virtualhospital;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
    private boolean check;
    private int order;


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

        adapter = new FileAdapter<>(this, R.layout.textview, files);
        ListView list = (ListView)findViewById(R.id.file_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, position);
            }
        });

        RequestParams params = new RequestParams();
        params.put("access_token", token);
        VHRestClient.post("medicalHistory/list", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    JSONObject object;
                    MedicalReports insreport;
                    for (int i = 0; i < response.length(); i++) {
                        insreport = new MedicalReports();
                        try {
                            object = response.getJSONObject(i);
                            insreport.setId(object.getInt("id"));
                            insreport.setName(object.getString("title"));
                            files.add(insreport);
                        } catch (JSONException e) {
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.delete_error + responseString, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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


    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.add_files).getId()){
            getFile();
        }
    }

    public void getFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
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
        RequestParams params = new RequestParams();
        try{
            params.put("access_token", token);
            params.put("new_medical_file", afile);
            VHRestClient.post("medicalHistory/upload", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progress.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), R.string.success_upload, Snackbar.LENGTH_LONG).show();

                    /*AlertDialog.Builder resend = new AlertDialog.Builder(UpdateHealthRecord.this);
                    resend.setTitle(getString(R.string.send_file));
                    resend.setMessage(getString(R.string.failed_upload) + "\n" + responseString + "\n" + getString(R.string.failed_upload2));
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
                    resendDialog.show();*/
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
            else {
                file = retrieveFile(position);
                viewFile(file);
            }
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
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("id", report.getId());
        Toast.makeText(this, R.string.download, Toast.LENGTH_LONG).show();
        VHRestClient.post("medicalHistory/getById", params, new FileAsyncHttpResponseHandler(afile) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Snackbar.make(findViewById(android.R.id.content), R.string.fail_download, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Snackbar.make(findViewById(android.R.id.content), R.string.success_download, Snackbar.LENGTH_LONG).show();
            }
        });
        return afile;
    }

    public void viewFile(File afile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(afile), "application/pdf");
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, R.string.pdf_viewer, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteFileFromServer(int position){
        order = position;
        MedicalReports report = files.get(order);
        if(report.getId() > 0) {
            RequestParams params = new RequestParams();
            params.put("access_token", token);
            params.put("id", report.getId());
            VHRestClient.post("medicalHistory/removeById", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.delete_error + responseString, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    deleteFileFromDir(order);
                }
            });
        }

        else
            deleteFileFromDir(position);
    }

    public void deleteFileFromDir(int position){
        String name = files.get(position).getName();
        File file = searchForFile(name);
        if(file == null){
            deleteFileFromArray(position);
            Toast.makeText(this, R.string.success_delete, Toast.LENGTH_LONG).show();
        }

            //Toast.makeText(this, R.string.file_not_found, Toast.LENGTH_LONG).show();
        else{
            if(file.delete()){
                file.delete();
                deleteFileFromArray(position);
                Toast.makeText(this, R.string.success_delete, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, R.string.fail_delete, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteFileFromArray(int position){
        files.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void showPopup(View v, int position){
        order = position;
        PopupMenu popup = new PopupMenu(this, v);
        Menu menu = popup.getMenu();
        menu.add(R.string.open_menuitem);
        menu.add(R.string.delete_menuitem);
        //menu.add(R.string.save_menuitem);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                itemClicked(order);
                return false;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDeleteDialog(order);
                return false;
            }
        });
        /*menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });*/
        popup.show();
    }

    public void showDeleteDialog(int position){
        order = position;
        AlertDialog.Builder delete= new AlertDialog.Builder(UpdateHealthRecord.this);
        delete.setTitle(getString(R.string.delete_file));
        delete.setMessage(getString(R.string.delete_message));
        delete.setCancelable(false);
        delete.setPositiveButton(R.string.delete_yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteFileFromServer(order);
            }
        });

        delete.setNegativeButton(R.string.delete_no_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog deleteDialog = delete.create();
        deleteDialog.show();
    }
}
