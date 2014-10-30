package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronAsync extends AsyncTask<Void, Void, Void> {

    ProgressDialog dialog;
    Context context;

    public PadronAsync( Context context ) {

        this.context = context;

        dialog = new ProgressDialog( this.context );
        dialog.setMessage( this.context.getString(R.string.padron_msg_new ) );
        dialog.setIndeterminate( false );
        dialog.setCancelable( false );
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        new PadronDAO( this.context ).padronLocal();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        dialog.dismiss();
    }

}