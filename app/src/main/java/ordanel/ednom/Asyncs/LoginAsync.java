package ordanel.ednom.Asyncs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.DAO.Login;
import ordanel.ednom.Entity.Ubigeo;
import ordanel.ednom.UbigeoVerify;

/**
 * Created by OrdNael on 28/10/2014.
 */
public class LoginAsync extends AsyncTask<String, ArrayList<Ubigeo>, ArrayList<Ubigeo>> {

    Context context;
    ProgressDialog dialog;

    public LoginAsync(Context context){
        this.context = context;

        dialog = new ProgressDialog(context);
        dialog.setMessage("Autenticando...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void err_login(){
        Toast toast = Toast.makeText( this.context, "Error: El password es incorrecto", Toast.LENGTH_SHORT );
        toast.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Ubigeo> doInBackground(String... params) {

        String password = params[0];

        return new Login().CheckLogin( password );
    }

    @Override
    protected void onPostExecute(ArrayList<Ubigeo> ubigeos) {
//        super.onPostExecute(ubigeos);
        dialog.dismiss();

        if ( ubigeos != null )
        {
            Intent intent = new Intent(this.context, UbigeoVerify.class);
            intent.putParcelableArrayListExtra( "listUbigeo", ubigeos );
            this.context.startActivity(intent);
        }
        else
        {
            err_login();
        }

    }
}