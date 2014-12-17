package ordanel.ednom.Asyncs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import ordanel.ednom.Business.InstrumentoBL;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.MainActivity;

/**
 * Created by OrdNael on 17/12/2014.
 */
public class ListadoInventarioFichaAsync extends AsyncTask<Integer, ArrayList<DocentesE>, ArrayList<DocentesE> > {


    Context context;

    public ListadoInventarioFichaAsync( Context paramContext ) {
        this.context = paramContext;
    }

    @Override
    protected ArrayList<DocentesE> doInBackground(Integer... params) {

        Integer nro_aula, offset, limit;

        nro_aula = params[0];
        offset = params[1];
        limit = params[2];

        try
        {
            Thread.sleep( 1500 );
        }
        catch ( Exception e )
        {
            Log.e( "ListadoInventarioFichaAsync", e.getMessage() );
        }

        return new InstrumentoBL( this.context ).listadoInventarioFicha( nro_aula, offset, limit );
    }

    @Override
    protected void onPostExecute(ArrayList<DocentesE> docentesEs) {

        Intent intent = new Intent( this.context, MainActivity.class );
        intent.putParcelableArrayListExtra( "listadoInventarioFicha", docentesEs );
        this.context.startActivity( intent );

    }

}