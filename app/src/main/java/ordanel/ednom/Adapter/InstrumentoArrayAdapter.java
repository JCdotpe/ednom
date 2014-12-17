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
import ordanel.ednom.Holders.InstrumentoHolder;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 17/12/2014.
 */
public class InstrumentoArrayAdapter extends ArrayAdapter<DocentesE> {

    private static final String TAG = DocentesArrayAdapter.class.getSimpleName();

    private LayoutInflater layoutInflater;

    public InstrumentoArrayAdapter(Context context, ArrayList<DocentesE> objects) {
        super(context, 0, objects);
        layoutInflater = layoutInflater.from( context );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DocentesE docentesE = getItem( position );

        InstrumentoHolder instrumentoHolder;

        try
        {
            if ( convertView == null )
            {
                instrumentoHolder = new InstrumentoHolder();

                convertView = layoutInflater.inflate(R.layout.body_two_listview, null );

                instrumentoHolder.setTextView1( (TextView) convertView.findViewById( R.id.itemDNI ) );
                instrumentoHolder.setTextView2( (TextView) convertView.findViewById( R.id.itemCodigo ) );
                instrumentoHolder.setTextView3( (TextView) convertView.findViewById( R.id.itemNombreCompleto ) );
                instrumentoHolder.setTextView4( (TextView) convertView.findViewById( R.id.itemNroAula ) );
                instrumentoHolder.setTextView5( (TextView) convertView.findViewById( R.id.itemFechaRegistro ) );

                convertView.setTag( instrumentoHolder );

            }
            else
            {
                instrumentoHolder = (InstrumentoHolder) convertView.getTag();
            }


            instrumentoHolder.getTextView1().setText( docentesE.getNro_doc() );
            instrumentoHolder.getTextView2().setText( docentesE.getCod_ficha() );
            instrumentoHolder.getTextView3().setText( docentesE.getNombreCompleto() );
            instrumentoHolder.getTextView4().setText( docentesE.getAulaLocalE().getNro_aula().toString() );
            instrumentoHolder.getTextView5().setText( docentesE.getF_ficha() );

        }
        catch ( Exception e )
        {
            Log.e( TAG, e.toString() );
        }

        return convertView;


    }
}
