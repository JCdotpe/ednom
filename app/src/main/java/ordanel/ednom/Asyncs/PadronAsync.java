package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronAsync extends AsyncTask<Integer, Integer, Integer> {

    ProgressDialog dialog;
    Context context;

    public PadronAsync( Context context ) {

        this.context = context;

        dialog = new ProgressDialog( this.context );
        dialog.setMessage( this.context.getString(R.string.padron_msg_download) );
        dialog.setIndeterminate( false );
        dialog.setCancelable( false );
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... params) {

        Integer idVersion = params[0];
        return new PadronDAO( this.context ).padronLocal( idVersion );
    }

    @Override
    protected void onPostExecute(Integer success) {
        super.onPostExecute(success);

        dialog.dismiss();

        if ( success.equals(0) && success.equals(-1) )
        {
            Toast toast = Toast.makeText( this.context, this.context.getString( R.string.padron_msg_error ), Toast.LENGTH_LONG );
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText( this.context, "Pasa al menu!", Toast.LENGTH_SHORT );
            toast.show();
        }

    }

}