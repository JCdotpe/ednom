package ordanel.ednom.Asyncs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.Business.SedeOperativaBL;
import ordanel.ednom.R;
import ordanel.ednom.UbigeoVerify;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class LoginAsync extends AsyncTask< String, Integer, Integer > {

    Context context;
    ProgressDialog dialog;

    public LoginAsync( Context context ) {
        this.context = context;

        dialog = new ProgressDialog( this.context );
        dialog.setMessage( this.context.getString( R.string.login_msg ) );
        dialog.setIndeterminate( false );
        dialog.setCancelable( false );
        dialog.show();
    }

    @Override
    protected Integer doInBackground( String... params ) {

        return new SedeOperativaBL( this.context ).checkLogin( params );

    }

    @Override
    protected void onPostExecute( Integer error ) {

        dialog.dismiss();

        if ( error == 0 )
        {
            Intent intent = new Intent( this.context, UbigeoVerify.class );
            /*intent.putParcelableArrayListExtra( "listUbigeo", ubigeoEs); // metodo para enviar arraylist*/
            this.context.startActivity( intent );
        }
        else
        {
            err_login( error );
        }

    }

    public void err_login( Integer error ) {

        String msg_error = "";

        switch ( error )
        {
            case 1:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_conexion);
                break;
            case 2:
                msg_error = this.context.getString( R.string.login_msg_error_password );
                break;
            case 3:
                msg_error = this.context.getString( R.string.login_msg_error_data);
                break;
            case 4:
                msg_error = this.context.getString( R.string.login_msg_error_register );
                break;
        }

        Toast.makeText( this.context, msg_error, Toast.LENGTH_LONG ).show();
    }
}