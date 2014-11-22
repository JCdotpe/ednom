package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.Business.PadronBL;
import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronAsync extends AsyncTask<VersionE, Integer, Integer> {

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
    protected Integer doInBackground(VersionE... paramVersionE) {

        return new PadronBL( this.context ).asyncPadron( paramVersionE[0] );
    }

    @Override
    protected void onPostExecute(Integer error) {
        super.onPostExecute(error);

        dialog.dismiss();

        if ( error == 0 )
        {
            Intent intent = new Intent( this.context, MainActivity.class);
            this.context.startActivity( intent );
        }
        else
        {
            err_padron( error );
        }

    }

    public void err_padron( Integer error ) {

        String msg_error = "";

        switch ( error )
        {
            case 1:
                msg_error = this.context.getString( R.string.padron_msg_error_local );
                break;

            case 2:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_conexion );
                break;

            case 3:
                msg_error = this.context.getString( R.string.padron_msg_error_data );
                break;

            case 4:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_data );
                break;

            case 5:
                msg_error = this.context.getString( R.string.padron_msg_error_clean );
                break;

            case 6:
                msg_error = this.context.getString( R.string.padron_msg_error_register );
                break;

            case 7:
                msg_error = this.context.getString( R.string.version_error_register );
                break;
        }

        Toast.makeText( this.context, msg_error, Toast.LENGTH_LONG ).show();
    }

}