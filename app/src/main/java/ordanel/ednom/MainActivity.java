package ordanel.ednom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.Entity.Ubigeo;
import ordanel.ednom.libreria.HttpPostAux;

public class MainActivity extends Activity {

    EditText txtUsuario;
    EditText txtPassword;
    Button btnLogin;
    HttpPostAux posteo;

    ArrayList<Ubigeo> arrayList;

    String IP_Server = "172.16.100.45";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/acces.php";

//    String IP_Server = "jc.pe";
//    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/acces.php";

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posteo = new HttpPostAux();

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = txtUsuario.getText().toString();
                String password = txtPassword.getText().toString();

                if ( checkLoginData( usuario, password ) == true )
                {
                    new asyncLogin().execute(usuario, password);
                }
                else
                {
                    err_login();
                }
            }
        });

    }

    public boolean checkLoginData(String userName, String password){

        if ( userName.equals("") || password.equals("") )
        {
            Log.e("Login UI", "check login data user or pass error!");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void err_login(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast = Toast.makeText( getApplicationContext(), "Error: El password es incorrecto", Toast.LENGTH_SHORT );
        toast.show();
    }


    public ArrayList<Ubigeo>loginStatus(String usuario, String password){

        ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
        parametersPost.add( new BasicNameValuePair("usuario", usuario) );
        parametersPost.add( new BasicNameValuePair("password", password) );

        JSONArray jsonArray = posteo.getServerData( parametersPost, URL_Connect );

        SystemClock.sleep(950); // luego retirar

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            arrayList = new ArrayList<Ubigeo>();
            JSONObject jsonObject;

            try
            {
                for ( int i = 0; i < jsonArray.length(); i++ )
                {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    Ubigeo ubigeo = new Ubigeo();
                    ubigeo.setDepartamento( jsonObject.getString("Departamento") );
                    ubigeo.setProvincia( jsonObject.getString("Provincia") );
                    ubigeo.setDistrito( jsonObject.getString("Distrito") );
                    ubigeo.setLocal( jsonObject.getString("Local") );

                    arrayList.add(ubigeo);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return arrayList;
        }
        else
        {
            return null;
        }

    }

    class asyncLogin extends AsyncTask<String, ArrayList<Ubigeo>, ArrayList<Ubigeo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Autenticando...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<Ubigeo> doInBackground(String... params) {

            String usuario, password;

            usuario = params[0];
            password = params[1];

            return loginStatus(usuario, password);
        }

        @Override
        protected void onPostExecute(ArrayList<Ubigeo> ubigeos) {
            super.onPostExecute(ubigeos);

            dialog.dismiss();

            if ( ubigeos != null )
            {
                Integer count = ubigeos.size();
                Log.e("onPostExecute : ", count.toString() );

                Intent intent = new Intent(MainActivity.this, UbigeoVerify.class);
                // intent.putExtra( "listUbigeo", ubigeos );
                intent.putParcelableArrayListExtra( "listUbigeo", ubigeos );

                startActivity(intent);
            }
            else
            {
                err_login();
            }
        }
    }

}

