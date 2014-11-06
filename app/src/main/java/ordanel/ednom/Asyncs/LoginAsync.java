package ordanel.ednom.Asyncs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.DAO.LoginDAO;
import ordanel.ednom.Entity.UsuarioLocalE;
import ordanel.ednom.R;
import ordanel.ednom.UbigeoVerify;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class LoginAsync extends AsyncTask< String, ArrayList<UsuarioLocalE>, ArrayList<UsuarioLocalE> > {

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

    public void err_login() {
        Toast toast = Toast.makeText( this.context, this.context.getString( R.string.login_msg_error ), Toast.LENGTH_SHORT );
        toast.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<UsuarioLocalE> doInBackground( String... params ) {

        String password = params[0];

        return new LoginDAO( this.context ).CheckLogin( password );
    }

    @Override
    protected void onPostExecute( ArrayList<UsuarioLocalE> ubigeoEs) {

        dialog.dismiss();

        if ( ubigeoEs != null )
        {
            Intent intent = new Intent( this.context, UbigeoVerify.class );
            intent.putParcelableArrayListExtra( "listUbigeo", ubigeoEs);
            this.context.startActivity( intent );
        }
        else
        {
            err_login();
        }

    }
}