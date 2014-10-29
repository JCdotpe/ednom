package ordanel.ednom.Asyncs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import ordanel.ednom.DAO.VersionDAO;
import ordanel.ednom.ObtainCensus;

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
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        Intent intent = new Intent( this.context, ObtainCensus.class );
        intent.putExtra( "statusVersion", integer );
        this.context.startActivity( intent );

    }
}
