package care.hospital.virtual.virtualhospital;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by semsem on 27/04/16.
 */
public class FileAdapter<T> extends ArrayAdapter<T> {
    private View view;
    private Context myContext;
    private T object;
    private List <T> list;

    public FileAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        myContext = context;
        list = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row, parent, false);

        ImageView icon = (ImageView)row.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.pdf3);

        TextView label = (TextView)row.findViewById(R.id.filename);
        object = list.get(position);
        label.setText(object.toString());


        return row;
    }
}
