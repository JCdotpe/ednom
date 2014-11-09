package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.DAO.PadronDAO;
import ordanel.ednom.MainActivity;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronAsync extends AsyncTask<Void, Integer, Integer> {

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
    protected Integer doInBackground(Void... params) {

        return new PadronDAO( this.context ).padronLocal();
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
                msg_error = this.context.getString( R.string.padron_msg_error_nube );
                break;
            case 2:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_data );
                break;
            case 3:
                msg_error = this.context.getString( R.string.padron_msg_error_register );
                break;
        }

        Toast toast = Toast.makeText( this.context, msg_error, Toast.LENGTH_SHORT );
        toast.show();
    }

}