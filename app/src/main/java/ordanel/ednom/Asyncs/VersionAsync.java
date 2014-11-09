package ordanel.ednom.Asyncs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ordanel.ednom.DAO.VersionDAO;
import ordanel.ednom.MainActivity;
import ordanel.ednom.ObtainCensus;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionAsync extends AsyncTask< Void, Integer, Integer> {

    private static final String TAG =  VersionAsync.class.getSimpleName();

    Context context;

    public VersionAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        return new VersionDAO( this.context ).checkVersion();

    }

    @Override
    protected void onPostExecute(Integer statusVersion) {
        super.onPostExecute(statusVersion);

        if ( statusVersion == 99 )
        {
            Intent intent = new Intent( this.context, ObtainCensus.class );
            /*intent.putExtra( "statusVersion", statusVersion );*/
            this.context.startActivity( intent );
        }
        else if ( statusVersion == 100 )
        {
            Intent intent = new Intent( this.context, MainActivity.class);
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
                msg_error = this.context.getString( R.string.version_error_register );
                break;
        }

        Toast toast = Toast.makeText( this.context, msg_error, Toast.LENGTH_SHORT );
        toast.show();
    }
}