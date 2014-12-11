package ordanel.ednom.ListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ordanel.ednom.R;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private LayoutInflater layoutInflater;

    public CustomArrayAdapter( Context context, List<String> objects ) {
        super(context, 0, objects);
        layoutInflater = layoutInflater.from( context );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if ( convertView == null )
        {
            holder = new Holder();

            convertView = layoutInflater.inflate( R.layout.listview, null );
            holder.setTextView1( (TextView) convertView.findViewById( R.id.textView1 ) );
            holder.setTextView2( (TextView) convertView.findViewById( R.id.textView2 ) );

            convertView.setTag( holder );
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.getTextView1().setText( getItem( position ) );
        holder.getTextView2().setText( getItem( position ) );

        return  convertView;
    }
}