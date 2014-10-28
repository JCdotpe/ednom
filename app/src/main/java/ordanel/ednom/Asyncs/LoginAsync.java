package ordanel.ednom.Asyncs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import ordanel.ednom.DAO.Login;
import ordanel.ednom.Entity.Ubigeo;

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Ubigeo> doInBackground(String... params) {

        String usuario = params[0];
        String password = params[1];

        return new Login().CheckLogin(usuario, password);
    }

    @Override
    protected void onPostExecute(ArrayList<Ubigeo> ubigeos) {
//        super.onPostExecute(ubigeos);
        dialog.dismiss();
    }
}
