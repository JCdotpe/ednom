package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.Business.VersionBL;
import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.Library.NetworkUtils;
import ordanel.ednom.MainActivity;
import ordanel.ednom.ObtainCensus;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionAsync extends AsyncTask< Void, VersionE, VersionE> {

    Context context;
    ProgressDialog dialog;

    public VersionAsync(Context context) {
        this.context = context;

        dialog = new ProgressDialog( this.context );
        dialog.setMessage( this.context.getString( R.string.version_validate ) );
        dialog.setIndeterminate( false );
        dialog.setCancelable( false );
        dialog.show();

    }

    @Override
    protected VersionE doInBackground(Void... params) {

        NetworkUtils networkUtils = new NetworkUtils();
        Boolean connection = networkUtils.haveNetworkConnection( this.context );

        if ( connection )
        {
            return new VersionBL( this.context ).checkVersion();
        }
        else
        {
            return new VersionBL( this.context ).checkVersionOffline();
        }

    }

    @Override
    protected void onPostExecute(VersionE versionE) {
        super.onPostExecute(versionE);

        dialog.dismiss();

        Integer statusVersion = versionE.getStatus();

        if ( statusVersion == 99 )
        {
            Intent intent = new Intent( this.context, ObtainCensus.class );
            intent.putExtra( "versionE", versionE );
            /*intent.putParcelableArrayListExtra( "listUbigeo", ubigeoEs); // metodo para enviar arraylist*/
            this.context.startActivity( intent );
        }
        else if ( statusVersion == 100 )
        {
            Intent intent = new Intent( this.context, MainActivity.class );
            this.context.startActivity( intent );
        }
        else
        {
            err_login( statusVersion );
        }

    }

    public void err_login( Integer error ) {

        String msg_error = "";

        switch ( error )
        {
            case 1:
                msg_error = this.context.getString( R.string.version_error_local );
                break;
            case 2:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_conexion );
                break;
            case 3:
                msg_error = this.context.getString( R.string.httppostaux_msg_error_data );
                break;
            case 4:
                msg_error = this.context.getString( R.string.version_error_check );
                break;

            case 5:
                msg_error = this.context.getString( R.string.version_error_not_data );
                break;
        }

        Toast.makeText( this.context, msg_error, Toast.LENGTH_LONG ).show();
    }
}