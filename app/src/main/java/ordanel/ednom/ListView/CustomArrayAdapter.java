package ordanel.ednom.ListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class CustomArrayAdapter extends ArrayAdapter<DocentesE> {

    private static final String TAG = CustomArrayAdapter.class.getSimpleName();

    private LayoutInflater layoutInflater;

    public CustomArrayAdapter( Context context, ArrayList<DocentesE> objects ) {
        super(context, 0, objects);
        layoutInflater = layoutInflater.from( context );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DocentesE docentesE = getItem( position );

        Holder holder;

        try
        {
            if ( convertView == null )
            {
                holder = new Holder();

            /*convertView = layoutInflater.inflate( R.layout.listview, null );*/
                convertView = layoutInflater.inflate( R.layout.body_one_listview, null );
            /*holder.setTextView1( (TextView) convertView.findViewById( R.id.textView1 ) );
            holder.setTextView2( (TextView) convertView.findViewById( R.id.textView2 ) );*/
                holder.setTextView1( (TextView) convertView.findViewById( R.id.itemDNI ) );
                holder.setTextView2( (TextView) convertView.findViewById( R.id.itemNombreCompleto ) );
                holder.setTextView3( (TextView) convertView.findViewById( R.id.itemNroAula ) );
                holder.setTextView4( (TextView) convertView.findViewById( R.id.itemFechaRegistro ) );

                convertView.setTag( holder );
            }
            else
            {
                holder = (Holder) convertView.getTag();
            }

            holder.getTextView1().setText( docentesE.getNro_doc() );
            holder.getTextView2().setText( docentesE.getNombreCompleto() );
            holder.getTextView3().setText( docentesE.getAulaLocalE().getNro_aula().toString() );
            holder.getTextView4().setText( docentesE.getF_registro() );

        }
        catch (Exception e)
        {
            Log.e( TAG, e.toString() );
        }

        return  convertView;

    }
}