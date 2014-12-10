package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ordanel.ednom.Business.PadronBL;

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

    }

}
