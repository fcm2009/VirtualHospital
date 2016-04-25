package care.hospital.virtual.virtualhospital;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

public class SendFile extends Service {
    public SendFile() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        File newFile = new File(intent.getData().getPath());
        RequestParams params = new RequestParams();
        try {
            params.put("new_medical_file", newFile);
        }
        catch(FileNotFoundException e){}

        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }
}
