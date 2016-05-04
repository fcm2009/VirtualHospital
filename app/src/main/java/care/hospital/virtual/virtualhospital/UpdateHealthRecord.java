package care.hospital.virtual.virtualhospital;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import care.hospital.virtual.virtualhospital.util.UriParser;
import care.hospital.virtual.virtualhospital.util.VHRestClient;
import cz.msebera.android.httpclient.Header;

public class UpdateHealthRecord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int REQUEST_FOR_FILE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ProgressDialog progress;
    private String token;


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

        verifyStoragePermissions(this);

        SharedPreferences auth = getSharedPreferences("token", 0);
        token = auth.getString("access_token", null);

        ListView list = (ListView)findViewById(R.id.file_list);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);

        fetchMedicalHistoriesMeta();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_FOR_FILE) {
                try {
                    File medicalHistory = cacheFile(result.getData());
                    sendMedicalHistory(medicalHistory);
                } catch(IOException e) {
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void fetchMedicalHistoriesMeta() {
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        VHRestClient.post("medicalHistory/list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                ListView list = (ListView) findViewById(R.id.file_list);
                list.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return response.length();
                    }

                    @Override
                    public Object getItem(int position) {
                        try {
                            return response.getJSONObject(position);
                        } catch (JSONException e) {
                            Log.e(getClass().getName(), e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if(convertView == null) {
                            convertView = getLayoutInflater().inflate(R.layout.row, parent, false);
                        }
                        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
                        icon.setImageResource(R.drawable.pdf3);

                        TextView label = (TextView) convertView.findViewById(R.id.filename);
                        try {
                            label.setText(((JSONObject) getItem(position)).getString("title"));
                        } catch (JSONException e) {
                            Log.e(getClass().getName(), e.getMessage());
                        }

                        return convertView;
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Snackbar.make(findViewById(android.R.id.content), R.string.data_fetch_error + responseString, Snackbar.LENGTH_LONG).show();
            }

        });
    }

    public void sendMedicalHistory(final File file) {
        progress = ProgressDialog.show(this, getString(R.string.send_file), getString(R.string.waiting), true);
        try {
            RequestParams params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("access_token", token);
            params.put("new_medical_file", file);
            VHRestClient.post("medicalHistory/upload", params, new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    progress.dismiss();
                    fetchMedicalHistoriesMeta();
                    Toast.makeText(UpdateHealthRecord.this, R.string.success_upload, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progress.dismiss();

                    AlertDialog.Builder resend = new AlertDialog.Builder(UpdateHealthRecord.this);
                    resend.setTitle(getString(R.string.send_file));
                    resend.setMessage(getString(R.string.failed_upload) + "\n" + responseString + "\n" + getString(R.string.failed_upload2));
                    resend.setCancelable(false);
                    resend.setPositiveButton(R.string.resend_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            sendMedicalHistory(file);
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
            });
        }
        catch(FileNotFoundException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    public void retrieveMedicalHistory(int id) {
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("id", id);

        File destinationDir = new File(Environment.getExternalStorageDirectory() +
                "/" + Environment.DIRECTORY_DOWNLOADS);
        Toast.makeText(this, R.string.download, Toast.LENGTH_LONG).show();
        VHRestClient.post("medicalHistory/getById", params, new FileAsyncHttpResponseHandler(destinationDir) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                viewMedicalHistory(response);
                Toast.makeText(UpdateHealthRecord.this, R.string.success_download, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Snackbar.make(findViewById(android.R.id.content), R.string.fail_download, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void viewMedicalHistory(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/png");
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, R.string.pdf_viewer, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteMedicalHistory(int id){
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("id", id);
        VHRestClient.post("medicalHistory/removeById", params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(UpdateHealthRecord.this, responseString, Toast.LENGTH_LONG).show();
                fetchMedicalHistoriesMeta();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(UpdateHealthRecord.this, R.string.data_fetch_error + responseString, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showDeleteDialog(final int id){
        AlertDialog.Builder delete = new AlertDialog.Builder(UpdateHealthRecord.this);
        delete.setTitle(getString(R.string.delete_file));
        delete.setMessage(getString(R.string.delete_message));
        delete.setCancelable(false);
        delete.setPositiveButton(R.string.delete_yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteMedicalHistory(id);
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

    private File cacheFile(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        String fileName = UriParser.getName(this, uri);
        return cacheFile(inputStream, fileName);
    }

    private File cacheFile(File input) {
        try {
            FileInputStream inputStream = new FileInputStream(input);
            return cacheFile(inputStream, input.getName());
        } catch (FileNotFoundException e) {
            Log.e(getClass().getName(), e.getMessage());
            return null;
        }
    }

    private File cacheFile(InputStream inputStream, String fileName) {
        try {
            File outputFile = File.createTempFile(fileName, null, getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();

            return outputFile;
        } catch (FileNotFoundException e) {
            Log.e(getClass().getName(), e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FOR_FILE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemId = -1;
        try {
            itemId = ((JSONObject) parent.getItemAtPosition(position)).getInt("id");
        } catch (JSONException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        retrieveMedicalHistory(itemId);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int itemId = -1;
        try {
            itemId = ((JSONObject) parent.getItemAtPosition(position)).getInt("id");
        } catch (JSONException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        showDeleteDialog(itemId);
        return true;
    }
}
