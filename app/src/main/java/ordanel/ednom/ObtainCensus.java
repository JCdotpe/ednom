package ordanel.ednom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Leandro on 27/10/2014.
 */
public class ObtainCensus extends Activity {

    Button btnPadron;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtain_census);

        btnPadron = (Button) findViewById(R.id.btnPadron);

        btnPadron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new asyncPadron().execute();

                Toast toast = Toast.makeText(getApplicationContext(), "Padrón descargado", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

    class asyncPadron extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ObtainCensus.this);
            dialog.setMessage("Obteniendo Padrón");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            SystemClock.sleep(950);
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
        }


    }

}
