package ordanel.ednom.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Holders.DocentesHolder;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class DocentesArrayAdapter extends ArrayAdapter<DocentesE> {

    private static final String TAG = DocentesArrayAdapter.class.getSimpleName();

    private LayoutInflater layoutInflater;

    public DocentesArrayAdapter(Context context, ArrayList<DocentesE> objects) {
        super(context, 0, objects);
        layoutInflater = layoutInflater.from( context );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DocentesE docentesE = getItem( position );

        DocentesHolder docentesHolder;

        try
        {
            if ( convertView == null )
            {
                docentesHolder = new DocentesHolder();

                convertView = layoutInflater.inflate( R.layout.body_one_listview, null );

                docentesHolder.setTextView1( (TextView) convertView.findViewById( R.id.itemDNI ) );
                docentesHolder.setTextView2( (TextView) convertView.findViewById( R.id.itemNombreCompleto ) );
                docentesHolder.setTextView3( (TextView) convertView.findViewById( R.id.itemNroAula ) );
                docentesHolder.setTextView4( (TextView) convertView.findViewById( R.id.itemFechaRegistro ) );

                convertView.setTag(docentesHolder);
            }
            else
            {
                docentesHolder = (DocentesHolder) convertView.getTag();
            }

            docentesHolder.getTextView1().setText( docentesE.getNro_doc() );
            docentesHolder.getTextView2().setText( docentesE.getNombreCompleto() );
            docentesHolder.getTextView3().setText( docentesE.getAulaLocalE().getNro_aula().toString() );
            docentesHolder.getTextView4().setText( docentesE.getF_registro() );

        }
        catch (Exception e)
        {
            Log.e( TAG, e.toString() );
        }

        return  convertView;

    }
}