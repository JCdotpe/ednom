package ordanel.ednom.Asyncs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.Business.PadronBL;
import ordanel.ednom.Library.NetworkUtils;

/**
 * Created by OrdNael on 26/11/2014.
 */
public class SyncAsync extends AsyncTask<Void, Void, Void> {

    ProgressDialog dialog;
    Context context;

    public SyncAsync( Context paramContext ) {

        this.context = paramContext;

        dialog = new ProgressDialog( paramContext );
        dialog.setMessage( "Sincronizando" );
        dialog.setIndeterminate( false );
        dialog.setCancelable( false );
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        new PadronBL( this.context ).getAllforSync();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (NetworkUtils.haveNetworkConnection(this.context)) {
            Toast.makeText(context, "Se han sincronizados los datos correctamente", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder =  new AlertDialog.Builder(this.context);
            builder.setTitle("Importante");
            builder.setMessage("No se pudo sincronizar los datos porque no hay conexión con el servidor. Revise su conexión e inténtelo nuevamente");
            builder.setPositiveButton("OK", null);
            builder.create();
            builder.show();
        }
    }

}
