package care.hospital.virtual.virtualhospital;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by semsem on 24/04/16.
 */
public class UriParser {

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor;

            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                /*String str [] = new String[10];
                for(int i = 0; i < cursor.getColumnCount(); i++)
                    str[i] = cursor.getColumnName(i);*/
                int cursorIndex = cursor.getColumnIndex("_display_name");

                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    return cursor.getString(cursorIndex);
                }
            }
            catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }

    public static String nameFromUri(String uri){
        return "";
    }
}
