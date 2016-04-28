package care.hospital.virtual.virtualhospital;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by semsem on 24/04/16.
 */
public class UriParser {

    public static String getName(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                int cursorIndex = cursor.getColumnIndex("_display_name");

                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    String name = cursor.getString(cursorIndex);
                    cursor.close();
                    return name;
                }
            }
            catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme()))
            return new File(uri.getPath()).getName();

        Toast.makeText(context, R.string.inappropriate_file, Toast.LENGTH_LONG).show();
        return null;
    }

    public static String nameFromUri(String uri){
        return "";
    }
}
