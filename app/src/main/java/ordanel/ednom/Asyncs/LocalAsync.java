package ordanel.ednom.Asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import ordanel.ednom.DAO.LocalDAO;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Interfaces.MainI;
import ordanel.ednom.R;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class LocalAsync extends AsyncTask<String, ArrayList<PadronE>, ArrayList<PadronE>> {

    private MainI mainI;

    ProgressDialog progressDialog;

    Context context;

    public LocalAsync( Context context ) {

        this.context = context;

        progressDialog = new ProgressDialog( this.context );
        progressDialog.setMessage( this.context.getString(R.string.docente_found ) );
        progressDialog.setIndeterminate( false );
        progressDialog.setCancelable( false );
        progressDialog.show();

    }

    public void setMainI( MainI mainI ) {
        this.mainI = mainI;
    }

    @Override
    protected ArrayList<PadronE> doInBackground(String... params) {

        String number_dni = params[0];
        return new LocalDAO( this.context  ).searchPerson( number_dni );

    }

    @Override
    protected void onPostExecute(ArrayList<PadronE> padronEs) {
        super.onPostExecute(padronEs);

        progressDialog.dismiss();
        mainI.showPerson( padronEs );

    }
}